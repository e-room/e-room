const AWS = require('aws-sdk')
const querystring = require('querystring')
const S3 = new AWS.S3({
    signatureVersion: 'v4',
    region: 'ap-northeast-2',
})
const sharp = require('sharp')
const supportImageTypes = ['jpg', 'jpeg', 'png'];


// set the S3 and API GW endpoints
exports.handler = async (event, context, callback) => {
    const {response, request} = event.Records[0].cf
    const {uri, origin} = request
    const BUCKET = "s3://e-room-dev/thumbnail/"
    const params = new URLSearchParams(request.querystring)

    console.log('Request key: %s', uri.slice(1))

    if (response.status == 200) {
        const params = querystring.parse(request.querystring);
        if (!params.s || !params.t || !params.q) {
            callback(null, response);
            return;
        }

        let key = decodeURIComponent(request.uri).substring(1);

        let width, height, type, quality, requiredFormat;

        const sizeMatch = params.s.split('x');
        const typeMatch = params.t;
        const qualityMatch = params.q;

        let originalFormat = key.match(/(.*)\.(.*)/)[2].toLowerCase();
        if (!supportImageTypes.some((type) => {
            return type == originalFormat
        })) {
            responseUpdate(
                403,
                'Forbidden',
                'Unsupported image type',
                [{key: 'Content-Type', value: 'text/plain'}],
            );
            callback(null, response);
        }

        width = parseInt(sizeMatch[0], 10);
        height = parseInt(sizeMatch[1], 10);
        type = typeMatch == 'crop' ? 'cover' : typeMatch;
        quality = parseInt(qualityMatch, 10)

        // correction for jpg required for 'Sharp'
        originalFormat = originalFormat == 'jpg' ? 'jpeg' : originalFormat;
        requiredFormat = originalFormat;

        try {
            const s3Object = await S3.getObject({
                Bucket: BUCKET,
                Key: key,
            }).promise();
            if (s3Object.ContentLength == 0) {
                responseUpdate(404, 'Not Found', 'The image does not exist', [{
                    key: 'Content-Type',
                    value: 'text/plain'
                }]);
                callback(null, response);
            }

            let metaData, resizedImage, byteLength = 0;
            if (requiredFormat != 'jpeg' && requiredFormat != 'png') {
                requiredFormat = 'jpeg';
            }

            while (1) {
                resizedImage = await sharp(s3Object.Body).rotate();
                metaData = await resizedImage.metadata();

                if (metaData.width > width || metaData.height > height) {
                    resizedImage
                        .resize(width, height, {fit: type});
                }
                if (byteLength >= 1046528 || originalFormat != requiredFormat) {
                    resizedImage
                        .toFormat(requiredFormat, {quality: quality});
                }
                resizedImage = await resizedImage.toBuffer();

                byteLength = Buffer.byteLength(resizedImage, 'base64');
                if (byteLength >= 1046528) {
                    quality -= 10;
                } else {
                    break;
                }
            }

            responseUpdate(
                200,
                'OK',
                resizedImage.toString('base64'),
                [{key: 'Content-Type', value: 'image/' + requiredFormat}],
                'base64'
            );
            response.headers['cache-control'] = [{key: 'cache-control', value: 'max-age=31536000'}];
            return callback(null, response);

        } catch (error) {
            console.error(error);
            return callback(error)
        }

    } else {
        callback(null, response);
    }

    function responseUpdate(status, statusDescription, body, contentHeader, bodyEncoding = undefined) {
        response.status = status;
        response.statusDescription = statusDescription;
        response.body = body;
        response.headers['content-type'] = contentHeader;
        if (bodyEncoding) {
            response.bodyEncoding = bodyEncoding;
        }
    }
}

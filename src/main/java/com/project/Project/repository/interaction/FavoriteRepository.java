package com.project.Project.repository.interaction;

import com.project.Project.domain.interaction.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
}

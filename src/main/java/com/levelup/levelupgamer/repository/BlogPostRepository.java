package com.levelup.levelupgamer.repository;

import com.levelup.levelupgamer.entity.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Integer> {
    Optional<BlogPost> findBySlug(String slug);
    List<BlogPost> findByPublicadoTrueOrderByFechaPublicacionDesc();
    List<BlogPost> findAllByOrderByCreatedAtDesc();
}

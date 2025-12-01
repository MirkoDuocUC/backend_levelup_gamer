package com.levelup.levelupgamer.service;

import com.levelup.levelupgamer.entity.BlogPost;
import com.levelup.levelupgamer.entity.Usuario;
import com.levelup.levelupgamer.repository.BlogPostRepository;
import com.levelup.levelupgamer.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogPostRepository blogPostRepository;
    private final UsuarioRepository usuarioRepository;

    public List<BlogPost> obtenerPostsPublicados() {
        return blogPostRepository.findByPublicadoTrueOrderByFechaPublicacionDesc();
    }

    public BlogPost obtenerPorSlug(String slug) {
        BlogPost post = blogPostRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Post no encontrado"));
        
        // Incrementar vistas
        post.setVistas(post.getVistas() + 1);
        return blogPostRepository.save(post);
    }

    @Transactional
    public BlogPost crearPost(String email, BlogPost post) {
        Usuario autor = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        post.setAutor(autor);
        if (post.getPublicado()) {
            post.setFechaPublicacion(LocalDateTime.now());
        }
        
        return blogPostRepository.save(post);
    }

    @Transactional
    public BlogPost actualizarPost(Integer id, BlogPost datos) {
        BlogPost post = blogPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post no encontrado"));
        
        post.setTitulo(datos.getTitulo());
        post.setContenido(datos.getContenido());
        post.setResumen(datos.getResumen());
        post.setImagenDestacada(datos.getImagenDestacada());
        post.setCategoria(datos.getCategoria());
        post.setTags(datos.getTags());
        
        if (!post.getPublicado() && datos.getPublicado()) {
            post.setFechaPublicacion(LocalDateTime.now());
        }
        post.setPublicado(datos.getPublicado());
        
        return blogPostRepository.save(post);
    }

    public void eliminarPost(Integer id) {
        blogPostRepository.deleteById(id);
    }
}

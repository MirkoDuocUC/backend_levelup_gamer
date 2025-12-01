package com.levelup.levelupgamer.service;

import com.levelup.levelupgamer.entity.Contacto;
import com.levelup.levelupgamer.repository.ContactoRepository;
import com.levelup.levelupgamer.util.EstadoContacto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactoService {

    private final ContactoRepository contactoRepository;

    public Contacto enviarMensaje(Contacto contacto) {
        contacto.setEstado(EstadoContacto.nuevo);
        return contactoRepository.save(contacto);
    }

    public List<Contacto> obtenerTodos() {
        return contactoRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Contacto> obtenerPorEstado(EstadoContacto estado) {
        return contactoRepository.findByEstadoOrderByCreatedAtDesc(estado);
    }

    @Transactional
    public Contacto responderMensaje(Integer id, String respuesta) {
        Contacto contacto = contactoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado"));
        
        contacto.setRespuesta(respuesta);
        contacto.setFechaRespuesta(LocalDateTime.now());
        contacto.setEstado(EstadoContacto.respondido);
        
        return contactoRepository.save(contacto);
    }
}

package com.aluracursos.forohub.domain.topico;

import com.aluracursos.forohub.domain.curso.Curso;
import com.aluracursos.forohub.domain.curso.CursoRepository;
import com.aluracursos.forohub.domain.usuario.Usuario;
import com.aluracursos.forohub.domain.usuario.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    public Topico registrarTopico (DatosRegistroTopico datosRegistroTopico) {
        Usuario usuario = obtenerUsuario(Long.valueOf(datosRegistroTopico.idUsuario()));
        Curso curso = obtenerCursoPorNombre(datosRegistroTopico.nombreCurso());

        return topicoRepository.save(new Topico(datosRegistroTopico, usuario, curso));
    }

    public Page<DatosListadoTopico> listarTopicos(Pageable paginacion) {
        return topicoRepository.findAll(paginacion).map(DatosListadoTopico::new);
    }

    @Transactional
    public Topico actualizarTopico(Long id, DatosActualizarTopico datosActualizarTopico){
        Topico topico = obtenerTopico(id);
        Usuario usuario;
        Curso curso;

        if(datosActualizarTopico.idUsuario() != null){
            usuario = obtenerUsuario(Long.valueOf(datosActualizarTopico.idUsuario()));
            topico.setUsuario(usuario);

        }

        if (datosActualizarTopico.nombreCurso() != null) {
            curso = obtenerCursoPorNombre(datosActualizarTopico.nombreCurso());
            topico.setCurso(curso);
        }

        topico.actualizar(datosActualizarTopico);

        return topico;

    }

    public void eliminarTopico(Long id) {
        Topico topico = obtenerTopico(id);
        if(topico != null){
            topicoRepository.deleteById(id);
        }
    }

    public DatosRespuestaTopico detallarTopico(Long id){
        Topico topico = obtenerTopico(id);
        return new DatosRespuestaTopico(topico);
    }

    private Usuario obtenerUsuario(Long idUsuario){
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
    }

    private Curso obtenerCursoPorNombre(String nombreCurso){
        return cursoRepository.findByNombre(nombreCurso)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado."));
    }

    private Topico obtenerTopico(Long idTopico){
        return topicoRepository.findById(idTopico)
                .orElseThrow(() -> new IllegalArgumentException("Topico no encontrado."));
    }

}

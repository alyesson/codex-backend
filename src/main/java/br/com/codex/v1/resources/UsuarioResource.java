package br.com.decamptech.v1.resources;

import br.com.decamptech.v1.domain.cadastros.Usuario;
import br.com.decamptech.v1.domain.dto.UsuarioDto;
import br.com.decamptech.v1.service.UsuarioService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "v1/api/usuarios")
public class UsuarioResource {

    @Autowired
    UsuarioService usuarioService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_TI', 'TI')")
    @PostMapping
    public ResponseEntity<UsuarioDto> create (@Valid @RequestBody UsuarioDto usuariodto){
        Usuario objUser = usuarioService.create(usuariodto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/id").buildAndExpand(objUser.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_TI', 'TI')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<UsuarioDto> update(@PathVariable Integer id, @Valid @RequestBody UsuarioDto usuariodto){
        Usuario objUser = usuarioService.update(id, usuariodto);
        return ResponseEntity.ok().body(new UsuarioDto(objUser));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_TI', 'TI')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UsuarioDto>delete(@PathVariable Integer id){
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_TI', 'TI')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<UsuarioDto>findById(@PathVariable Integer id){
        Usuario objUser = usuarioService.findById(id);
        return ResponseEntity.ok().body(new UsuarioDto(objUser));
    }

    @GetMapping(value = "/cpf/{cpf}")
    public ResponseEntity<UsuarioDto>findByCpf(@PathVariable String cpf){
        Usuario objUser = usuarioService.findByCpf(cpf);
        return ResponseEntity.ok().body(new UsuarioDto(objUser));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_TI', 'TI')")
    @GetMapping
    public ResponseEntity<List<UsuarioDto>> findAll(){
        List<Usuario> list = usuarioService.findAll();
        List<UsuarioDto> listaUser = list.stream().map(UsuarioDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listaUser);
    }

    @PutMapping(value = "/troca_senha/{cpf}/{senha}")
    public ResponseEntity<String> changePassword(@PathVariable String cpf, @PathVariable String senha) {
        usuarioService.changePassword(cpf, senha);
        return ResponseEntity.ok().body("");
    }

    @PostMapping(value = "/recupera_senha")
    public ResponseEntity<Void> recoverPassword(@RequestParam String email, @RequestParam String baseUrl){
        usuarioService.recoverPassword(email, baseUrl);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/email/{email}")
    public ResponseEntity<UsuarioDto>findByEmail(@PathVariable String email){
        Usuario objUser = usuarioService.findByEmail(email);
        return ResponseEntity.ok().body(new UsuarioDto(objUser));
    }

    /*@PostMapping("/primeiro_acesso")
    public ResponseEntity<UsuarioDto> first_access (@Valid @RequestBody UsuarioDto usuariodto){
        Usuario objUser = usuarioService.create(usuariodto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/id").buildAndExpand(objUser.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }*/

    @GetMapping(value = "/profissional/{perfis}")
    public ResponseEntity<List<UsuarioDto>> findByProfissional(@PathVariable Integer perfis){
        List<Usuario> list = usuarioService.findByPerfis(perfis);
        List<UsuarioDto> listaUser = list.stream().map(UsuarioDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listaUser);
    }

    //Conta o número de profissionais cadastrados
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO')")
    @GetMapping(value = "/total_clientes")
    public ResponseEntity<Integer> countByPerfis(@RequestParam(value = "perfis") String perfis){
        int totalDeClientes = usuarioService.countByPerfis(perfis);
        return ResponseEntity.ok(totalDeClientes);
    }

}

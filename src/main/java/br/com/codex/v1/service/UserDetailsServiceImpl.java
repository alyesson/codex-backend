package br.com.codex.v1.service;

import br.com.codex.v1.domain.cadastros.Pessoa;
import br.com.codex.v1.domain.repository.PessoaRepository;
import br.com.codex.v1.security.UserSS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        Optional<Pessoa> user = pessoaRepository.findByEmail(email);
        if(user.isPresent()){
            return new UserSS(user.get().getId(),user.get().getEmail(),user.get().getSenha(), user.get().getPerfis());
        }
        throw new UsernameNotFoundException(email);
    }
}

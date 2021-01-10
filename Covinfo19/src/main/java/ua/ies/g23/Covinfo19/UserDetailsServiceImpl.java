package ua.ies.g23.Covinfo19;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.Medico;
import ua.ies.g23.Covinfo19.pacientes_med_hosp.repository.MedicoRepository;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private MedicoRepository applicationUserRepository;

    public UserDetailsServiceImpl(MedicoRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Medico applicationUser = applicationUserRepository.findByNumeroMedico(username);
        if (applicationUser == null) {
            throw new UsernameNotFoundException(username);
        }
        //return new Medico(applicationUser.getNumero_medico(), applicationUser.getCodigo_acesso(), emptyList());
        return applicationUser;
    }
}
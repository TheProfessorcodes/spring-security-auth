package services;


import DTO.SignUpDTO;
import DTO.UserDTO;
import Entity.User;
import exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import repositories.UserRepo;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepo userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new BadCredentialsException("User with email "+ username +" not found"));
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with id "+ userId +
                " not found"));
    }

    public User getUsrByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public UserDTO signUp(SignUpDTO signUpDTO) {
        Optional<User> user = userRepository.findByEmail(signUpDTO.getEmail());
        if (user.isPresent()) {
            throw new BadCredentialsException("User with email already exits "+ signUpDTO.getEmail());
        }
        User userEntity = modelMapper.map(signUpDTO, User.class);
        userEntity.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
        User savedUser=userRepository.save(userEntity);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    public User save(User newUser) {
        return userRepository.save(newUser);
    }

}

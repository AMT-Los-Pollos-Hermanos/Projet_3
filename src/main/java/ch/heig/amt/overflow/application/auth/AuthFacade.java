package ch.heig.amt.overflow.application.auth;

import ch.heig.amt.overflow.domain.user.IUserRepository;
import ch.heig.amt.overflow.domain.user.User;
import ch.heig.amt.overflow.infrastructure.security.BCryptPasswordEncoder;

public class AuthFacade {

    IUserRepository userRepository;

    public AuthFacade(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(RegisterCommand command) throws RegistrationFailedException {
        User existingUser = userRepository.findByUsername(command.getUsername()).orElse(null);

        if (existingUser != null) {
            throw new RegistrationFailedException("Username is already used");
        }

        try {
            User newUser = User.builder()
                    .username(command.getUsername())
                    .firstName(command.getFirstName())
                    .lastName(command.getLastName())
                    .email(command.getEmail())
                    .clearTextPassword(command.getClearTextPassword())
                    .build();
            userRepository.save(newUser);
        } catch (Exception e) {
            throw new RegistrationFailedException(e.getMessage());
        }
    }

    public void changePassword(ChangePasswordCommand command) {

        User existingUser = userRepository.findById(command.getUserId()).orElse(null);
        String encryptedNewPassword = BCryptPasswordEncoder.hash(command.getNewPassword());

        if (existingUser != null) {
            if (BCryptPasswordEncoder.verify(command.getOldPassword(), existingUser.getEncryptedPassword())){
                    if(!BCryptPasswordEncoder.verify(command.getNewPassword(), existingUser.getEncryptedPassword())){
                            if( command.getNewPassword().equals(command.getNewPasswordAgain())) {
                                existingUser.setEncryptedPassword(encryptedNewPassword);
                                userRepository.save(existingUser);
                            }
                            else{
                                throw new ChangePasswordException("Le deux nouveaux mots de passe doivent être pareils");
                            }
                    }
                    else{
                        throw new ChangePasswordException("Le nouveau mot de passe doit être différent de l'ancien");
                    }
            }
            else{
                throw new ChangePasswordException("L'ancien mot de passe ne correspond pas");
            }
        } else {
            throw new RuntimeException("Utilisateur non trouvé");
        }

    }

    public UserDTO authenticate(AuthenticateCommand command) throws AuthenticationFailedException {
        User user = userRepository.findByUsername(command.getUsername()).orElse(null);

        if (!(user != null && user.authenticate(command.getClearTextPassword()))) {
            throw new AuthenticationFailedException("Invalid credentials");
        }

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }

}

package com.educacionit.digitalers.DTOs.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.educacionit.digitalers.DTOs.UserDTO;
import com.educacionit.digitalers.DTOs.services.UserDTOService;
import com.educacionit.digitalers.entities.User;
import com.educacionit.digitalers.repositories.UserRepository;

@Component
public class UserImplementDTO implements GenericRepositoryDTO<UserDTO, Long> {
	private static Logger logger = LogManager.getLogger();
	@Value("${database.key}")
	private String KEY_AES;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserDTOService userDTOService;

	public Optional<UserDTO> findById(Long id) {
		User user = userRepository.findById(id).orElse(null);
		Optional<UserDTO> optionalUserDTO = Optional.of(userDTOService.getUserDTO(user, null));
		return user == null ? null : optionalUserDTO;
	}

	public Optional<UserDTO> findByEmail(String email) {
		User user = userRepository.findByEmail(email).orElse(null);
		Optional<UserDTO> optionalUserDTO = Optional.of(userDTOService.getUserDTO(user, null));
		return user == null ? null : optionalUserDTO;
	}

	public Optional<UserDTO> save(UserDTO dto) {

		User user = userRepository.findByEmail(dto.getEmail()).orElse(null);
		logger.info(user);
		if (user == null) {
			user = userDTOService.getUser(dto);
			user.setCreationDate(LocalDateTime.now());
		}

		user.setActive(dto.getActive());
		user.setKey(CodeUtils.AES_Encrypt(dto.getKey(), user.getEmail().concat(KEY_AES)));

		logger.info(user);
		userRepository.save(user);
		return Optional.of(userDTOService.getUserDTO(user, null));
	}

	public void delete(UserDTO dto) {
		userRepository.delete(userDTOService.getUser(dto));
	}

	public List<UserDTO> findAll() {
		return userRepository.findAll().stream().map(e -> userDTOService.getUserDTO(e, null))
				.collect(Collectors.toList());
	}
}

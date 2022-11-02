package com.educacionit.digitalers.Controllers;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.educacionit.digitalers.DTOs.UserDTO;
import com.educacionit.digitalers.DTOs.repositories.UserImplementDTO;
import com.educacionit.digitalers.enums.MessageType;
import com.educacionit.digitalers.exceptions.ExceptionDTO;
import com.educacionit.digitalers.services.ResponseMessageService;

@RestController
@RequestMapping(value = { "/users" }, produces = { MediaType.APPLICATION_JSON_VALUE })
public class UserController implements GenericRestController<UserDTO, Long> {
	private static Logger logger = LogManager.getLogger();

	@Autowired
	private UserImplementDTO userImplementDTO;

	@Autowired
	private ResponseMessageService responseMessageService;

	public ResponseEntity<?> findById(Long id) {
		logger.info("ID : " + id);
		try {
			UserDTO userDTO = userImplementDTO.findById(id).orElse(null);
			return ResponseEntity.ok(userDTO);
		} catch (ExceptionDTO e) {
			logger.error(e);
			return ResponseEntity.status(404).body(responseMessageService.getResponseMessage(MessageType.NO_ELEMENTS,
					"Usuario con ID " + id + " No encontrado"));
		}
	}

	public ResponseEntity<?> insert(@Valid UserDTO userDTO, BindingResult bindingResult) {

		return save(userDTO, bindingResult);
	}

	public ResponseEntity<?> update(@Valid UserDTO userDTO, BindingResult bindingResult) {

		return save(userDTO, bindingResult);
	}

	public ResponseEntity<?> delete(@Valid UserDTO userDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.status(400)
					.body(responseMessageService.getResponseMessage(MessageType.VALIDATION_ERROR, bindingResult));
		}

		try {
			userImplementDTO.findByEmail(userDTO.getEmail());
		} catch (ExceptionDTO e) {
			logger.error(e);
			return ResponseEntity.status(404).body(
					responseMessageService.getResponseMessage(MessageType.NO_ELEMENTS, userDTO + " No encontrado"));
		}

		userImplementDTO.delete(userDTO);

		return ResponseEntity.ok(
				responseMessageService.getResponseMessage(MessageType.DELETE_ELEMENT, "Usuario " + userDTO.getEmail())
						+ " eliminado correctamente");
	}

	public ResponseEntity<?> findAll() {
		return ResponseEntity.ok(userImplementDTO.findAll());
	}

	private ResponseEntity<?> save(UserDTO userDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.status(400)
					.body(responseMessageService.getResponseMessage(MessageType.VALIDATION_ERROR, bindingResult));
		}
		logger.info(userDTO);
		userImplementDTO.save(userDTO);

		userDTO.setMessage("Usuario Guardado Exitosamente");
		return ResponseEntity.ok(userDTO);
	}
}

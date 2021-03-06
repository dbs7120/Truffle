package com.ssafy.controller;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.pjt.dto.AccountDto;
import com.ssafy.pjt.dto.EventDto;
import com.ssafy.pjt.dto.LoginRequestDto;
import com.ssafy.pjt.dto.ParticipationDto;
import com.ssafy.pjt.service.AccountServiceImpl;
import com.ssafy.pjt.service.TokenProvider;

import io.swagger.annotations.ApiOperation;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

/**
 * http://localhost:8000/truffle/swagger-ui.html
 */

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/account")
public class AccountController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);
	
	private static final String SUCCESS = "SUCCESS";
	private static final String FAIL = "FAIL";
	private static final String ACCESS_TOKEN = "access-token";
	private static final String X_AUTH_TOKEN = "x-auth-token";
	

	@Autowired
	private TokenProvider tokenProvider;

	@Autowired
	private AccountServiceImpl accountService;

	@ApiOperation(value = "?????????")
	@PostMapping("/login")
	private ResponseEntity<Map<String, Object>> login(@RequestBody final LoginRequestDto loginRequestDto) {
		Map<String, Object> resultMap = new HashMap<>();
		AccountDto accountDto = new AccountDto();
		accountDto.setEmail(loginRequestDto.getEmail());
		accountDto.setPassword(loginRequestDto.getPassword());
		try {
			AccountDto loginUser = accountService.login(accountDto);
			if (loginUser != null) {
				String token = tokenProvider.createToken();
				LOGGER.info(token);
				resultMap.put(ACCESS_TOKEN, token);
				resultMap.put("message", SUCCESS);
				resultMap.put("uuid", loginUser.getUuid());
				return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
			} else {
				resultMap.put("message", FAIL);
			}
		} catch (SQLException e) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "??????????????????", notes = "x-auth-token, email ??? ??????")
	@GetMapping("/accountInfo")
	private ResponseEntity<AccountDto> accountInfo(@RequestParam(required = true) final String email,
			HttpServletRequest request) {
		AccountDto accountDto;

		if (tokenProvider.validateToken(request.getHeader(X_AUTH_TOKEN))) {
			try {
				accountDto = accountService.accountInfo(email);
				return new ResponseEntity<>(accountDto, HttpStatus.OK);
			} catch (SQLException e) {
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			}
		} else {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
	}

	@ApiOperation(value = "????????????", notes = "uuid??? ?????????????????? ??????")
	@PostMapping("/signUp")
	private ResponseEntity<String> signUp(@RequestBody final AccountDto accountDto) {

		try {
			boolean result = accountService.signUp(accountDto);
			if (result) {
				return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
			}
		} catch (SQLException e) {
			return new ResponseEntity<>(FAIL, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(FAIL, HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "????????????", notes = "x-auth-token, email??? ??????")
	@DeleteMapping("/delete")
	private ResponseEntity<String> delete(@RequestParam(required = true) final String email,
			HttpServletRequest request) {
		if (tokenProvider.validateToken(request.getHeader(X_AUTH_TOKEN))) {
			try {
				boolean result = accountService.delete(email);
				if (result) {
					return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
				}
			} catch (SQLException e) {
				return new ResponseEntity<>(FAIL, HttpStatus.NO_CONTENT);
			}
		} else {
			return new ResponseEntity<>(FAIL, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(FAIL, HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "????????????", notes = "x-auth-token, email??? ??????, ??????????????????: address,address_detail,age,business_number,gender,nickname,password,phone")
	@PutMapping("/update")
	private ResponseEntity<String> update(@RequestBody final AccountDto accountDto, HttpServletRequest request) {
		if (tokenProvider.validateToken(request.getHeader(X_AUTH_TOKEN))) {
			try {
				boolean result = accountService.update(accountDto);
				if (result) {
					return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
				}
			} catch (SQLException e) {
				return new ResponseEntity<>(FAIL, HttpStatus.NO_CONTENT);
			}
		} else {
			return new ResponseEntity<>(FAIL, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(FAIL, HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "?????????????????? ???????????? ??????", notes = "email ??? ??????")
	@GetMapping("/selectEventWinnerByEmail")
	private ResponseEntity<List<EventDto>> selectEventWinnerByEmail(@RequestParam(required = true) final String email) {
		try {
			List<EventDto> list = accountService.selectEventWinnerByEmail(email);
			return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (SQLException e) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
	}

	@ApiOperation(value = "?????????????????? ???????????? ????????????", notes = "email ??? ??????")
	@GetMapping("/selectEventParticipationByEmail")
	private ResponseEntity<List<EventDto>> selectEventParticipationByEmail(
			@RequestParam(required = true) final String email) {
		try {
			List<EventDto> list = accountService.selectEventParticipationByEmail(email);
			return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (SQLException e) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
	}

	@ApiOperation(value = "????????????UUID??? ??????????????? ????????? ????????? ????????? ??????", notes = "uuid ??? ??????")
	@GetMapping("/selectCreateEventListByID")
	private ResponseEntity<List<EventDto>> selectCreateEventListByID(@RequestParam(required = true) final int uuid) {
		try {
			List<EventDto> list = accountService.selectCreateEventListByID(uuid);
			return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (SQLException e) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
	}

	@ApiOperation(value = "????????? ?????? ?????? ?????????", notes = "???????????? ????????? ????????? ?????????????????? ????????? ???????????? ???????????? ???????????? ?????? ??????")
	@GetMapping("/verifyPhoneNumber")
	private String verifyPhoneNumber(@RequestParam(required = true) final String phone) {
		Random rand;
		String numStr = "";
		try {
			rand = SecureRandom.getInstanceStrong();
			for (int i = 0; i < 4; i++) {
				String ran = Integer.toString(rand.nextInt(10));
				numStr += ran;
			}
		} catch (NoSuchAlgorithmException e) {
			return null;
		}

//		String api_key = "NCSIVOZHLC9Z7CJW";
//		String api_secret = "MJMUXFFTXNCDXYLCX2QECLGFQCMTEKOY";
		String api_key = "NCS7D6DG0ZSPCLMQ";
		String api_secret = "ZE4B0FYWKDV2Y2LARTVT8AQWTIM9FZ6Q";
		Message coolsms = new Message(api_key, api_secret);

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("to", phone); // ??????????????????
//		params.put("from", "01094927120"); // ??????????????????.
		params.put("from", "01032341661"); // ??????????????????.
		params.put("type", "SMS");
		params.put("text", "????????? ??????????????? ????????? ????????? : ???????????????" + "[" + numStr + "]" + "?????????.");
		params.put("app_version", "test app 1.2");

		try {
			JSONObject obj = (JSONObject) coolsms.send(params);
			LOGGER.info(obj.toString());
		} catch (CoolsmsException e) {
			return "error:coolsms????????????";
		}
		return numStr;
	}

	@ApiOperation(value = "??????????????????,uuid??? ???????????? ????????? ??????, ?????????????????? join_num 1?????? ??????", notes = "?????? ????????? ???????????????????????? ?????????????????? ??????????????? 204")
	@DeleteMapping("/cancelParticipation")
	private ResponseEntity<String> cancelParticipation(@RequestParam(required = true) final int event_id,
			@RequestParam(required = true) final int uuid) {
		ParticipationDto participationDto = new ParticipationDto();
		participationDto.setEvent_id(event_id);
		participationDto.setUuid(uuid);
		try {
			boolean result = accountService.cancelParticipation(participationDto);
			if (result) {
				return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
			}
		} catch (SQLException e) {
			return new ResponseEntity<>(FAIL, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(FAIL, HttpStatus.NO_CONTENT);
	}

}

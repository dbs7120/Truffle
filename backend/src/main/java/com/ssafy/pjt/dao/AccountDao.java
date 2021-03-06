package com.ssafy.pjt.dao;

import java.sql.SQLException;
import java.util.List;

import com.ssafy.pjt.dto.AccountDto;
import com.ssafy.pjt.dto.EventDto;
import com.ssafy.pjt.dto.ParticipationDto;

public interface AccountDao {
	public AccountDto login(AccountDto accountDto) throws SQLException;

	public AccountDto accountInfo(String email) throws SQLException;

	public int signUp(AccountDto accountDto) throws SQLException;

	public int delete(String email) throws SQLException;

	public int update(AccountDto accountDto) throws SQLException;

	public List<EventDto> selectEventWinnerByEmail(String email) throws SQLException;

	public List<EventDto> selectEventParticipationByEmail(String email) throws SQLException;

	public List<EventDto> selectCreateEventListByID(int uuid) throws SQLException;

	public int cancelParticipation(ParticipationDto participationDto) throws SQLException;
	
	public int leftEvent(int event_id) throws SQLException;
}

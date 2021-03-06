package com.ssafy.pjt.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.pjt.dao.EventDao;
import com.ssafy.pjt.dto.EventDto;
import com.ssafy.pjt.dto.EventImgFileDto;
import com.ssafy.pjt.dto.EventUserRequestDto;
import com.ssafy.pjt.dto.ParticipationDto;
import com.ssafy.pjt.dto.SearchDto;
import com.ssafy.pjt.dto.WinDto;

@Service
public class EventServiceImpl implements EventService {

	@Autowired
	private EventDao eventDao;

	@Override
	public List<EventDto> all() throws SQLException {
		return eventDao.all();
	}

	@Override
	public List<EventDto> detail(int event_id) throws SQLException {
		return eventDao.detail(event_id);
	}

	@Override
	public List<EventDto> selectByGender(int gender) throws SQLException {
		return eventDao.selectByGender(gender);
	}

	@Override
	public List<EventDto> selectByAge(int age) throws SQLException {
		return eventDao.selectByAge(age);
	}

	@Override
	public List<EventDto> selectByProduct(String product) throws SQLException {
		eventDao.upsertSearchHit(product);
		return eventDao.selectByProduct(product);
	}

	@Override
	public List<EventDto> selectByCategory(String category) throws SQLException {
		return eventDao.selectByCategory(category);
	}

	@Override
	public boolean update(EventDto eventDto) throws SQLException {
		return eventDao.update(eventDto) == 1;
	}

	@Override
	public boolean insert(EventDto eventDto, EventImgFileDto eventImgFileDto) throws SQLException {
		if (eventDao.insert(eventDto) == 1) {
			return eventDao.insertEventImg(eventImgFileDto) == 1;
		}
		return false;
	}

	@Override
	public boolean joinEvent(int event_id) throws SQLException {
		return eventDao.joinEvent(event_id) == 1;
	}

	@Override
	public List<EventUserRequestDto> selectWinListByEventId(int event_id) throws SQLException {
		return eventDao.selectWinListByEventId(event_id);
	}

	@Override
	public List<EventUserRequestDto> selectParticipationListByEventId(int event_id) throws SQLException {
		return eventDao.selectParticipationListByEventId(event_id);
	}

	@Override
	public List<SearchDto> selectSearchHit() throws SQLException {
		return eventDao.selectSearchHit();
	}

	@Override
	public boolean insertUserIdToParticipation(ParticipationDto participationDto) throws SQLException {
		return eventDao.insertUserIdToParticipation(participationDto) == 1;
	}

	@Override
	public boolean insertUserIdWinParticipation(WinDto winDto) throws SQLException {
		return eventDao.insertUserIdWinParticipation(winDto) == 1;
	}

	@Override
	public EventImgFileDto selectEventFileNameByEventID(int event_id) throws SQLException {
		return eventDao.selectEventFileNameByEventID(event_id);
	}

}

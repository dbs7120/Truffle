import { instance, posts } from './index';

//이벤트전체조회
function eventAll() {
  return instance.get('/event/all');
}
//이벤트 상세조회
function eventDetail(event_id) {
  return posts.get(`/event/detail?event_id=${event_id}`);
}
//이벤트 페이지 작성
function eventInsert(frm) {
  return posts.post('/event/insert', frm, { headers: { 'Access-Control-Allow-Origin': '*', 'Content-Type': 'multipart/form-data' } });
}
//이벤트 수정
function eventUpdate(event_data) {
  return posts.put('/event/update', event_data);
}

//나이별 상세 조회
function eventSelectAge(age) {
  return instance.get(`/event/selectByAge?age=${age}`);
}
//카테고리별 조회
function eventSelectCategory(category) {
  return instance.get(`/event/selectByCategory?category=${category}`);
}
//성별 상세 조회
function eventSelectGender(gender) {
  return instance.get(`/event/selectByGender?gender=${gender}`);
}
//상품명별 조회
function eventSelectProduct(product) {
  return instance.get(`/event/selectByProduct?product=${product}`);
}
//이벤트 참여자수 증가
function eventJoin(event_id) {
  return instance.put('/event/joinEvent', event_id, { headers: { 'Content-Type': 'application/json' } });
}

//특정이벤트 참여자 조회
function checkPartipants(event_id) {
  return posts.get(`/event/selectParticipationListByEventId?event_id=${event_id}`);
}
//특정이벤트 참여자응모시 추가
function createPartipants(partData) {
  return posts.post('/event/insertUserIdToParticipation', partData);
}

//특정이벤트 당첨자조회
function selectedWinner(event_id) {
  return posts.get(`/event/selectWinListByEventId?event_id=${event_id}`);
}

//특정이벤트 당첨자만들기
function createWinner(winData) {
  return posts.post('/event/insertUserIdWinParticipation', winData);
}

//이미지 자체로 반환
function returnImage(event_id) {
  return posts.get(`/event/selectEventImgFileEventID?event_id=${event_id}`);
}
//이미지 파일썸네일출력(base64)
function returnImage64(event_id) {
  return posts.get(`/event/selectEventFileBase64ByEventID?event_id=${event_id}`);
}
//상품조회
function searchProduct(product) {
  return posts.get(`/event/selectByProduct?product=${product}`);
}
//랭킹조회
function searchRank() {
  return instance.get('/event/selectSearchHit');
}
export {
  searchRank,
  searchProduct,
  returnImage64,
  returnImage,
  createWinner,
  selectedWinner,
  createPartipants,
  checkPartipants,
  eventAll,
  eventDetail,
  eventInsert,
  eventUpdate,
  eventSelectAge,
  eventSelectCategory,
  eventSelectGender,
  eventSelectProduct,
  eventJoin,
};

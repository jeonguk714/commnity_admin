package com.greenart.api;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.greenart.mapper.MemberMapper;
import com.greenart.utils.AESAlgorithm;
import com.greenart.vo.AdminVO;
import com.greenart.vo.LoginVO;
import com.greenart.vo.MemberVO;

@RestController
public class APIController {
	@Autowired
	MemberMapper mapper;
 	@PostMapping("/api/login")
	public Map<String, String> postLogin(@RequestBody LoginVO vo, HttpSession session) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		try {
			vo.setPwd(AESAlgorithm.Encrypt(vo.getPwd()));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		AdminVO admin = mapper.selectAdmin(vo);
		if(admin == null) {
			map.put("status", "failed");
			map.put("message", "아이디 혹은 비밀번호 오류입니다.");
		}
		else {
			if(admin.getUser_type() != 1) {
				map.put("status", "failed");
				map.put("message", "관리자 전용입니다.");
			}
			else {
				map.put("status", "success");
				map.put("message", "로그인에 성공하셨습니다.");
				session.setAttribute("admin", admin);
			}
		}
		return map;
	}
 	
 	@GetMapping("/api/members")
 	public List<MemberVO> getMembes() {
 		return mapper.selectMembers();
 	}
 	
 	@PostMapping("/api/add_member")
 	public Map<String, String> postAddMember(@RequestBody MemberVO vo) {
 		Map<String, String> map = new LinkedHashMap<String, String>();
 		
 		LoginVO loginInfo = new LoginVO();
 		loginInfo.setId(vo.getUi_id());
 		
 		Integer result = mapper.selectUserById(loginInfo);
 		if(result == 0) {
 			//갸입가능
 			try {vo.setUi_pwd(AESAlgorithm.Encrypt(vo.getUi_pwd())); } catch(Exception e) {}
 			mapper.insertMember(vo);
 			map.put("result", "success");
 			map.put("message", "사용자가 추가되었습니다.");
 		}
 		else {
 			//가입불가
 			map.put("result", "failed");
 			map.put("message", vo.getUi_id() + "는 이미 사용중인 아이디입니다.");
 		}
 		
 		return map;
 	}
 	
 	@PostMapping("/api/delete_member")
 	public Map<String, String> deleteMember(@RequestBody MemberVO vo) {
 		Map<String, String> map = new LinkedHashMap<String, String>();
 		mapper.deleteUser(vo.getUi_seq());
 		map.put("result", "success");
 		
 		return map;
 	}
 	
 	@PostMapping("/api/update_user")
 	public Map<String, String> postUpdateUser(@RequestBody MemberVO vo) {
 		Map<String, String> map = new LinkedHashMap<String, String>();
 		mapper.updateUser(vo);
 		map.put("result", "success");
 		return map;
 	}
 	
 	@PostMapping("/api/change_pwd")
 	public Map<String, String> postChangePwd(@RequestBody MemberVO vo) throws Exception {
 		Map<String, String> map = new LinkedHashMap<String, String>();
 		
 		String encrypted = vo.getUi_pwd();
 		encrypted = AESAlgorithm.Encrypt(encrypted);
 		vo.setUi_pwd(encrypted);
 		
 		mapper.changeUserPassword(vo);
 		map.put("result", "success");
 		map.put("message", vo.getUi_id() + "의 비밀번호가 변경되었습니다.");
 		
 		return map;
 	}
}

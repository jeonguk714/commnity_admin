package com.greenart.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.greenart.vo.AdminVO;
import com.greenart.vo.LoginVO;
import com.greenart.vo.MemberVO;

@Mapper
public interface MemberMapper {
	public AdminVO selectAdmin(LoginVO vo);
	public List<MemberVO> selectMembers();
	public void insertMember(MemberVO vo);
	public Integer selectUserById(LoginVO vo);
	public void deleteUser(Integer seq);
	public void updateUser(MemberVO vo);
	public void changeUserPassword(MemberVO vo);
}

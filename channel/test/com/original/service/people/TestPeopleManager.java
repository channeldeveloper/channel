package com.original.service.people;

import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import com.channel.test.TestBase;
import com.original.service.channel.Account;

//获取所有联系人（按照名称，按照消息的ReceivedTime先后顺序）排序	Song	2013/2/6	2013-2-7
//获取一个联系人的所有消息，按照时间排序。	Song	2013-2-7	2013-2-8


public class TestPeopleManager extends TestBase {

	/**
	 * 获得联系人的个数。按照姓名排序。
	 */
	@Test
	public void testGetPeopleCount() {
		System.out.println(csc.getPeopleManager().getPeopleCount());
	}

	/**
	 * 获得所有联系人。按照姓名排序。
	 */
	@Test
	public void testGetPeoples() {

		List<People> pp = csc.getPeopleManager().getPeople();
		for (People p : pp) {
			System.out.println(p);
		}
	}

	/**
	 * 获得所有联系人按照分页(pageIndex，count)。按照姓名排序。
	 */
	@Test
	public void testGetPeopleByPage() {

		List<People> pp = csc.getPeopleManager().getPeople(0, 5);
		for (People p : pp) {
			System.out.println(p);
		}
		pp = csc.getPeopleManager().getPeople(1, 5);
		for (People p : pp) {
			System.out.println(p);
		}
		pp = csc.getPeopleManager().getPeople(100, 5);
		for (People p : pp) {
			System.out.println(p);
		}

	}

	/**
	 * 获得所有联系人。按照消息的先后排序。
	 */
	@Test
	public void testGetPeopleByDate() {
		List<People> pp = csc.getPeopleManager().getPeopleByDate();
		for (People p : pp) {
			System.out.println(p);
		}
	}

	/**
	 * 获得所有联系人按照分页(pageIndex，count)。按照消息的先后排序。
	 */
	@Test
	public void testGetPeopleByDateAndPage() {
		List<People> pp = csc.getPeopleManager().getPeopleByDate(0, 5);
		for (People p : pp) {
			System.out.println(p);
		}
		pp = csc.getPeopleManager().getPeopleByDate(1, 5);
		for (People p : pp) {
			System.out.println(p);
		}
		pp = csc.getPeopleManager().getPeopleByDate(10, 5);
		for (People p : pp) {
			System.out.println(p);
		}
	}
//	
//	"Message可以获得联系人，联系人可获得账号。
//	message-->peopleId-->People-->HashMap<String, Account> accountMap; SongXueyong"	Song	2013-2-10	2013-2-11
	@Test
	public void testGetPeopleAccount() {
		
		List<People> pp = csc.getPeopleManager().getPeople();
		for (People p : pp) {
			System.out.println(p);
			HashMap<String, Account> pa = p.getAccountMap();
			System.out.println(pa.size());
			System.out.println(pa);
		}
		
	}
}
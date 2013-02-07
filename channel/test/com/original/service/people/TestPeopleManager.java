package com.original.service.people;

import java.util.List;

import org.junit.Test;

import com.channel.test.TestBase;

public class TestPeopleManager extends TestBase {

	@Test
	public void testGetPeopleCount() {
		System.out.println(csc.getPeopleManager().getPeopleCount());
	}

	@Test
	public void testGetPeople() {

		List<People> pp = csc.getPeopleManager().getPeople();
		for (People p : pp) {
			System.out.println(p);
		}
	}

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

	@Test
	public void testGetPeopleByDate() {
		List<People> pp = csc.getPeopleManager().getPeopleByDate();
		for (People p : pp) {
			System.out.println(p);
		}
	}

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
}
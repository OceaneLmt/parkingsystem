package com.parkit.parkingsystem.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InputReaderUtilTest {

	private InputReaderUtil inputReaderUtil;

	@BeforeEach
	private void setUpPerTest() {
		inputReaderUtil = new InputReaderUtil();
	}

	@Test
	public void readSelectionTestWithGoodValueEntered() {
		ByteArrayInputStream bis = new ByteArrayInputStream("1\n".getBytes());
		Scanner scan = new Scanner(bis);
		inputReaderUtil.setScan(scan);
		assertEquals(1, inputReaderUtil.readSelection());
	}

	@Test
	public void readSelectionTestWithBadValueEntered() {
		ByteArrayInputStream bis = new ByteArrayInputStream("bonjour\n".getBytes());
		Scanner scan = new Scanner(bis);
		inputReaderUtil.setScan(scan);
		assertEquals(-1, inputReaderUtil.readSelection());
	}

	@Test
	public void readVehiculeRegNumberTestWithGoodValueEntered() throws Exception {
		ByteArrayInputStream bis = new ByteArrayInputStream("1234\n".getBytes());
		Scanner scan = new Scanner(bis);
		inputReaderUtil.setScan(scan);
		assertEquals("1234", inputReaderUtil.readVehicleRegistrationNumber());
	}

	@Test
	public void readVehiculeRegNumberTestWithBadValueEntered() throws Exception {
		ByteArrayInputStream bis = new ByteArrayInputStream(" ".getBytes());
		Scanner scan = new Scanner(bis);
		inputReaderUtil.setScan(scan);
		assertThrows(Exception.class, () -> inputReaderUtil.readVehicleRegistrationNumber());
	}
}

package info.svetlik.nervaak.simple.service.time.impl;

import org.springframework.stereotype.Service;

import info.svetlik.nervaak.simple.service.time.ClockService;

@Service
public class ClockServiceImpl implements ClockService {

	@Override
	public long getAsLong() {
		return System.nanoTime();
	}

}

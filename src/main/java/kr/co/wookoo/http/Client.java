package kr.co.wookoo.http;

import kr.co.wookoo.TraderResetTime;

import java.io.IOException;
import java.util.List;

public interface Client {

    List<TraderResetTime> fetchTraderResetTimes() throws IOException;
}

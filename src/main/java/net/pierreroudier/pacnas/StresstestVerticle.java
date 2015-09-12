package net.pierreroudier.pacnas;

import io.vertx.core.AbstractVerticle;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.datagram.DatagramSocketOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.*;

import java.util.HashMap;
import java.util.Map;

public class StresstestVerticle extends AbstractVerticle {
	private final Logger logger = LoggerFactory.getLogger(StresstestVerticle.class);
	private final static int port= 5353;
	private final static String host = "localhost";

	private final static int iterationCount = 50;
	private final static int waitTimeMs = 50;

	public void start() {

		Map<Integer, Measure> measureMap = new HashMap<>();

		try {
			logger.info("Starting StresstestVerticle");

			DatagramSocket socket = vertx.createDatagramSocket(new DatagramSocketOptions());
			socket.handler(datagram -> {
				long timestamp = System.nanoTime();
				try {
					Message receivedMessage = new Message(datagram.data().getBytes());
					int receivedId = receivedMessage.getHeader().getID();
					Measure measure = measureMap.get(receivedId);
					measure.endTimestamp = timestamp;
					measure.printTimeLaps(receivedId);
				} catch (Exception e) {
					logger.error("Error receiving", e);
				}
			});

			Name queryName = new Name("free.fr.");
			Record queryRecord = Record.newRecord(queryName, Type.A, DClass.IN);
			Message queryMessage = Message.newQuery(queryRecord);
			Cursor cursor = new Cursor();
			vertx.setPeriodic(waitTimeMs, yo  -> {
				int id = cursor.getPosition();
				queryMessage.getHeader().setID(id);
				Buffer buffer = Buffer.buffer(queryMessage.toWire(512));

				Measure m = new Measure();
				measureMap.put(id, m);
				m.startTimestamp = System.nanoTime();

				socket.send(buffer, port, host, asyncResult -> {
					if (asyncResult.failed()) {
						logger.info("Sending request failed");
					}
				});
			});




		} catch (Exception e) {
			logger.error("oups", e);
		}
	}

	public void stop() {
		logger.trace("Stopping StresstestVerticle");
	}
}

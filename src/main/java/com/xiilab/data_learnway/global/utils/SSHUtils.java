package com.xiilab.data_learnway.global.utils;

import java.io.IOException;
import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHUtils {
	private JSch jsch;
	private Session session;

	public SSHUtils() {
		jsch = new JSch();
	}

	/**
	 * SSH 서버에 연결
	 *
	 * @param username SSH 서버 사용자 이름
	 * @param host     SSH 서버 호스트
	 * @param port     SSH 서버 포트 (기본값: 22)
	 * @param password SSH 서버 암호
	 * @throws JSchException SSH 연결 중 오류 발생 시 던집니다.
	 */
	public void connect(String username, String host, int port, String password) throws JSchException {
		session = jsch.getSession(username, host, port);
		session.setPassword(password);
		// 명시된 시간 초과하면 에러 반환하는 timeout(ms) 시간 설정
		// session.setTimeout(5000);
		session.setConfig("StrictHostKeyChecking", "no"); // 호스트 키 체크 비활성화

		session.connect();
	}

	/**
	 * SSH 서버에 명령을 실행하고 실행 결과를 반환
	 *
	 * @param command 실행할 명령
	 * @return 명령 실행 결과 문자열
	 * @throws JSchException 명령 실행 중 오류 발생 시 던집니다.
	 * @throws IOException 입출력 오류 발생 시 던집니다.
	 */
	public String executeCommand(String command) throws JSchException, IOException {
		Channel channel = session.openChannel("exec");
		((ChannelExec)channel).setCommand(command);

		InputStream inputStream = channel.getInputStream();
		channel.connect();

		StringBuilder output = new StringBuilder();
		byte[] buffer = new byte[1024];
		int bytesRead;

		while ((bytesRead = inputStream.read(buffer)) != -1) {
			output.append(new String(buffer, 0, bytesRead));
		}

		channel.disconnect();

		return output.toString();
	}

	/**
	 * SSH 연결을 끊음
	 */
	public void disconnect() {
		if (session != null && session.isConnected()) {
			session.disconnect();
		}
	}
}

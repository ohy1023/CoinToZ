package com.example.financialfinalproject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    public static final String ePw = createKey();
    private final StringRedisTemplate redisTemplate;

    private MimeMessage createMessage(String to) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);
        message.setSubject("CoinToZ 인증 이메일 입니다.");

        String msgg = "<div style='margin: 100px auto; padding: 20px; max-width: 600px; font-family: Arial, sans-serif; border: 1px solid #ddd; border-radius: 10px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);'>"
                + "<p style='font-size: 16px; color: #555; text-align: center;'>"
                + "커뮤니티 기반 가상화폐 매매 및 매매일지 사이트 <strong>CoinToZ</strong>입니다.</p>"
                + "<hr style='border: 0; border-top: 1px solid #eee; margin: 20px 0;'>"
                + "<p style='font-size: 16px; color: #555; text-align: center;'>아래 코드를 인증창에 입력해 주세요:</p>"  // 가운데 정렬
                + "<div style='text-align: center; margin: 20px 0;'>"
                + "<span style='display: inline-block; padding: 10px 20px; background-color: #007BFF; color: white; font-size: 24px; font-weight: bold; border-radius: 5px;'>"
                + ePw + "</span>"
                + "</div>"
                + "<p style='font-size: 14px; color: #999; text-align: center;'>"
                + "이 인증 코드는 5분 동안 유효합니다.</p>"
                + "<p style='font-size: 12px; color: #aaa; text-align: center;'>"
                + "감사합니다.<br><strong>CoinToZ</strong> 팀</p>"
                + "</div>";

        message.setText(msgg, "utf-8", "html");
        message.setFrom(new InternetAddress("ohy971023@naver.com", "CoinToZ"));

        return message;
    }


    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    //  A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }
        return key.toString();
    }

    // 회원가입 인증 메시지 발송
    public String sendLoginAuthMessage(String to) throws Exception {
        log.info("email : {} ", to);
        MimeMessage message = createMessage(to);
        try{
            mailSender.send(message);
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        setDataExpire(ePw, to, 60*5L);
        return "인증 메일이 발송되었습니다.";
    }

    // redis
    // 인증번호 확인 하기
    public String getData(String key){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void setData(String key, String value) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    public void setDataExpire(String key, String value, long duration) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }
}
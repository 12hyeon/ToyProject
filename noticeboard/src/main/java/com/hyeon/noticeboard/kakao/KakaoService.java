package com.hyeon.noticeboard.kakao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import com.hyeon.noticeboard.web.MemberRepository;
import lombok.RequiredArgsConstructor; // 현재 스레드에 사용할 수 있는 실제 트랜잭션이 있는 EntityManager가 없습니다.
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional // persist가 안정적인 동작을 하기 위해서
@RequiredArgsConstructor
public class KakaoService {

    private final MemberRepository memberRepository;

    public String getToken(String code) throws IOException {
        // 인가코드로 토큰받기
        String host = "https://kauth.kakao.com/oauth/token";
        URL url = new URL(host);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        String token = "";
        try {
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true); // 데이터 기록 알려주기

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=${REST_API}");
            sb.append("&redirect_uri=http://${IP}:8080/kakao");
            sb.append("&code=" + code);

            bw.write(sb.toString());
            bw.flush();

            int responseCode = urlConnection.getResponseCode();
            System.out.println("responseCode = " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("result = " + result);

            // json parsing
            JSONParser parser = new JSONParser();
            JSONObject elem = (JSONObject) parser.parse(result);

            String access_token = elem.get("access_token").toString();
            String refresh_token = elem.get("refresh_token").toString();
            System.out.println("refresh_token = " + refresh_token);
            System.out.println("access_token = " + access_token);

            token = access_token;

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            throw new RuntimeException(e);
        }


        return token;
    }


    public KakaoDTO getUserInfo(String access_token) throws IOException {
        String host = "https://kapi.kakao.com/v2/user/me";
        //Map<String, Object> userInfo = new HashMap<>();
        KakaoDTO userInfo = KakaoDTO.builder().build();

        try {
            URL url = new URL(host);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Bearer " + access_token);
            urlConnection.setRequestMethod("GET");

            int responseCode = urlConnection.getResponseCode();

            System.out.println("responseCode = " + responseCode);


            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            String res = "";
            while((line=br.readLine())!=null)
            {
                res+=line;
            }

            System.out.println("res = " + res);


            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(res);
            JSONObject kakao_account = (JSONObject) obj.get("kakao_account");
            JSONObject properties = (JSONObject) obj.get("properties");
            JSONObject profile = (JSONObject) obj.get("profile");

            String id = obj.get("id").toString();
            String nickname = properties.get("nickname").toString();
            String email = kakao_account.get("email").toString();
            //String age_range = kakao_account.get("age_range").toString();

            // 전달 받은 값
            userInfo.setId(id);
            userInfo.setName(nickname);
            userInfo.setEmail(email);
            //userInfo.put("age_range", age_range);

            br.close();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        KakaoDTO result = memberRepository.findkakao(userInfo);
        // 위 코드는 먼저 정보가 저장되있는지 확인하는 코드.
        //System.out.println("S: name - " + result.getName() + ", email - " + result.getEmail());

        if(result==null) {
            // result가 null이면 정보가 저장이 안되있는거므로 정보를 저장.
            memberRepository.kakaoinsert(userInfo);
            // 위 코드가 정보를 저장하기 위해 Repository로 보내는 코드임.
            return memberRepository.findkakao(userInfo);
            // 위 코드는 정보 저장 후 컨트롤러에 정보를 보내는 코드임.
            //  result를 리턴으로 보내면 null이 리턴되므로 위 코드를 사용.
        } else {
            return result;
            // 정보가 이미 있기 때문에 result를 리턴함.
        }

    }

    public String getAgreementInfo(String access_token)
    {
        String result = "";
        String host = "https://kapi.kakao.com/v2/user/scopes";
        try{
            URL url = new URL(host);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Authorization", "Bearer "+access_token);

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            while((line=br.readLine())!=null)
            {
                result+=line;
            }

            int responseCode = urlConnection.getResponseCode();
            System.out.println("responseCode = " + responseCode);

            // result is json format
            br.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
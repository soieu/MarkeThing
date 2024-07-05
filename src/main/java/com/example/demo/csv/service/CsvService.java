package com.example.demo.csv.service;


import com.example.demo.entity.Market;
import com.example.demo.market.repository.MarketRepository;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.error.Mark;

@Service
@RequiredArgsConstructor
public class CsvService {

    private final MarketRepository marketRepository;

    @Transactional
    public void saveMarkets(){
        try{
            BufferedReader br = Files.newBufferedReader(Paths.get("/Users/sindongha/Desktop/market.csv"), Charset.forName("EUC-KR"));
            String line = "";

            while((line = br.readLine()) != null){
                List<String> stringList = new ArrayList<String>();
                String stringArray[] = line.split(",");
                stringList = Arrays.asList(stringArray);

                if(stringList.get(0).equals("식별번호")) continue; // 첫줄은 제외를 해야 함.
                if(stringList.size()!=7)continue; // 위도, 경도가 완전하지 않은 시장은 무시.

                int type = 0; // 상설장:0 5일장:1
                if(stringList.get(2).equals("5일장")){
                    type = 1;
                }
                for (String s : stringList) {
                    System.out.println(s);

                }

                GeometryFactory gf = new GeometryFactory(); // 포인트 객체를 처리하기 위한 클래스
                double lat = Double.parseDouble(stringList.get(5));
                double lot = Double.parseDouble(stringList.get(6));

                Market temp = Market.builder()
                        .idNum(Integer.parseInt(stringList.get(0)))
                        .type(type)
                        .marketName(stringList.get(1))
                        .roadAddress(stringList.get(3))
                        .streetAddress(stringList.get(4))
                        .point(gf.createPoint(new Coordinate(lat, lot)))
                        .build();
                // Market 데이터 생성
                marketRepository.save(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }




}

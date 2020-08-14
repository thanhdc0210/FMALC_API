package fmalc.api.util;

import fmalc.api.service.FuelTypeService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

@Component
public class FuelTypeUtil extends TimerTask {
    private final static String LINK = "https://petrolimex.com.vn";

    @Autowired
    FuelTypeService fuelTypeService;

    @Override
    public void run() {
        try {
            Document d = Jsoup.connect(LINK).timeout(6000).get();
            Elements ele = d.select("div#vie_p5_PortletContent");
            String content = ele.text();
            content = content.replace("Sản phẩm Vùng 1 Vùng 2 Xăng RON 95-IV ", "");
            content = content.replace("Xăng RON 95-III ", "");
            content = content.replace("E5 RON 92-II ", "");
            content = content.replace("DO 0,001S-V ", "");
            content = content.replace("DO 0,05S-II ", "");
            String[] lst = content.split(" ");
            List<Double> prices = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                if (i % 2 != 0) {
                    lst[i] = lst[i].replaceAll("[^0-9]+", "");
                    prices.add(Double.parseDouble(lst[i]));
                }


//                for (int i = 0; i < 10; i++) {
//                    if (i % 2 != 0) {
//                        lst[i] = lst[i].replaceAll("[^0-9]+", "");
//                        prices.add(Double.parseDouble(lst[i]));
//                    }
//
                }

                fuelTypeService.createOrUpdateFuelType(prices);
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }

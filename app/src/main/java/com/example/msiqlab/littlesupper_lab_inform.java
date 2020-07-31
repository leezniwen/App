package com.example.msiqlab;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class littlesupper_lab_inform extends AppCompatActivity {

    private littlesupper_Onboard_Adapter onboard_adapter;
    private LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.littlesupper_activity_lab_inform);
        setOnboard_Items();
        ViewPager2 onviewpager = findViewById(R.id.viewpager2_1);
        onviewpager.setAdapter(onboard_adapter);

    }

    private void setOnboard_Items(){
        List<littlesupper_Onboard_ITEM> onboardItems = new ArrayList<>();
        littlesupper_Onboard_ITEM test1 = new littlesupper_Onboard_ITEM();
        test1.setTitle("機構配備");
        test1.setContent("使用機構實驗室設備可以加速找出產品的弱點；在機構測試中，除了測定產品本身機構的設計之外，測試產品包裝包材的強度也是機構測試的重點。\n 1.) 震動實驗設備：振動測試可以確認產品在壽命周期中，是否能承受運輸或使用過程的振動環境的考驗，也能確定產品設計是否有達到要求標準。\n 2.) 落摔實驗設備：可以評估包材的結構以及包覆能力，以合理的包材成本出貨，減少不必要的包材。");
        test1.setImage(R.drawable.image1);

        littlesupper_Onboard_ITEM test2 = new littlesupper_Onboard_ITEM();
        test2.setTitle("環測配備");
        test2.setContent("MSI環測設備，能模擬產品可能面臨到的各種極端環境，\n對產品施加嚴苛環境條件，加速顯現老化因子於產品，使產品快速顯現脆弱的部份，\n以確保產品在保固期間內，在預期的使用、運輸或是儲存等環境下，都能保持功能可靠性以及安全性。");
        test2.setImage(R.drawable.image2);

        littlesupper_Onboard_ITEM test3 = new littlesupper_Onboard_ITEM();
        test3.setTitle("熱流配備");
        test3.setContent("幫助產品在設計階段即能以嚴苛的環境、驗證條件找出產品對熱的極限值\n，以確保產品符合客戶所需規格，上市後不會產生冒煙、發燙、熔毀或爆炸的狀況發生。");
        test3.setImage(R.drawable.image3);

        littlesupper_Onboard_ITEM test4 = new littlesupper_Onboard_ITEM();
        test4.setTitle("無響室配備");
        test4.setContent("為了避免電子產品或是資訊產品產生令人不適的噪音，造成不好的使用者體驗，所以需要了解產品運轉時所會產生的雜音。\n" +
                "無響室能隔絕環境背景中所產生的雜音，以獨立出產品所產生的聲音，藉此確定產品的聲音品質水準，並採取相應分析，提高產品品質。");
        test4.setImage(R.drawable.image4);

        littlesupper_Onboard_ITEM test5 = new littlesupper_Onboard_ITEM();
        test5.setTitle("電子配備");
        test5.setContent("目前還正在思考中。。。");
        test5.setImage(R.drawable.image5);

        littlesupper_Onboard_ITEM test6 = new littlesupper_Onboard_ITEM();
        test6.setTitle("音像配備");
        test6.setContent("\n" +
                "可靠度暨聲學實驗室為確保用戶與PA(Personal Assistant)之間，語音對話的體驗能達到 Microsoft 以及Amazon的規格。\n 實驗室符合歐洲 ETSI ES 202 396-1 規範，並為PAL / Microsoft / Amazon 所認可之驗證實驗室。");
        test6.setImage(R.drawable.image6);

        onboardItems.add(test1);
        onboardItems.add(test2);
        onboardItems.add(test3);
        onboardItems.add(test4);
        onboardItems.add(test5);
        onboardItems.add(test6);


        onboard_adapter = new littlesupper_Onboard_Adapter(onboardItems);

    }
}

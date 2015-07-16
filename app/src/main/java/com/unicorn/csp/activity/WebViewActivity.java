package com.unicorn.csp.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jauker.widget.BadgeView;
import com.malinskiy.materialicons.widget.IconTextView;
import com.unicorn.csp.R;
import com.unicorn.csp.activity.base.ToolbarActivity;
import com.unicorn.csp.other.greenmatter.ColorOverrider;
import com.unicorn.csp.utils.ToastUtils;

import butterknife.Bind;
import butterknife.OnClick;

public class WebViewActivity extends ToolbarActivity {

    @Bind(R.id.webView)
    WebView webView;

    @Bind(R.id.itv_thumb)
    IconTextView itvThumb;

    @Bind(R.id.itv_star)
    IconTextView itvStar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initToolbar(getIntent().getStringExtra("title"), true);
        initViews();

    }

    private void initViews() {

        initWebView();

        BadgeView badgeView = new BadgeView(this);
        badgeView.setTargetView(findViewById(R.id.comment_badge));
        badgeView.setBadgeCount(22);
    }

    private void initWebView() {

//        String url = "http://www.sc.xinhuanet.com/content/2015-07/08/c_1115856116.htm";

        String source ="<p><span style=\"font-size:20px\"><span style=\"background-color:rgb(255,255,255); color:rgb(36,36,36)\">　　近年来，福建省漳州市两级法院以落实人民陪审员&ldquo;倍增计划&rdquo;为契机，不断创新和加强人民陪审工作，通过为人民陪审员履职创造条件，既用其&ldquo;专长&rdquo;，更隆其&ldquo;待遇&rdquo;。数据显示，近五年来，漳州市人民陪审员参审案件的陪审率从最初的50%逐步上升到如今的97.62%。</span><br />\n" +
                "<br />\n" +
                "<span style=\"background-color:rgb(255,255,255); color:rgb(36,36,36)\">　　&ldquo;五员&rdquo;定位 &ldquo;全能&rdquo;陪审</span><br />\n" +
                "<br />\n" +
                "<span style=\"background-color:rgb(255,255,255); color:rgb(36,36,36)\">　　今年5月5日，在漳州市龙文区人民法院，记者见到了人民陪审员陈家富。作为全市唯一一个通过司法考试的人民陪审员，钻研法律、陪审案件、庭外调解,凡是职业法官做的事情，他一样都没&ldquo;落单&rdquo;。过去的10年，他参审民事、刑事案件近300起。</span><br />\n" +
                "<br />\n" +
                "<span style=\"background-color:rgb(255,255,255); color:rgb(36,36,36)\">　　陈家富还有另一个身份&mdash;&mdash;龙文区环境卫生管理处党支部书记。在他看来，人民陪审员的职责应该是多维的，除了履好陪审、调解等职责，更要发挥主观能动性，搭建司法为民的桥梁。2012年10月，在他的推动下，全省首个环卫工人维权岗在龙文区法院挂牌成立。</span><br />\n" +
                "<br />\n" +
                "<span style=\"background-color:rgb(255,255,255); color:rgb(36,36,36)\">　　&ldquo;龙文区800多名环卫工人承担700多万平方米路面的保洁任务，他们平均年龄55岁左右，安全意识和法律意识都不强，一旦发生事故，很难维权。&rdquo;陈家富告诉记者，维权难、保障不到位，成为环卫工人面临的现实困境。</span><br />\n" +
                "<br />\n" +
                "<span style=\"background-color:rgb(255,255,255); color:rgb(36,36,36)\">　　为此，在参审的过程中，他有意识地和法官探讨相关问题，提出建议。最终，在法院的支持下，环卫工人维权岗挂牌成立。该平台联合法院、交警、环卫、保险等部门，形成交警快速出警、法院快审快结、环卫部门及时办理等&ldquo;第一时间&rdquo;反应机制和沟通协调机制。</span><br />\n" +
                "<br />\n" +
                "<span style=\"background-color:rgb(255,255,255); color:rgb(36,36,36)\">　　&ldquo;关键还得在源头上预防，比如加强环卫工人岗前培训、安全教育等，依托这个平台，已经培训了10多次。&rdquo;陈家富告诉记者，在该平台的运作下，环卫工人在工资待遇、工伤保险等方面有了长足的改善，事故发生率也大幅降低。</span><br />\n" +
                "<br />\n" +
                "<span style=\"background-color:rgb(255,255,255); color:rgb(36,36,36)\">　　&ldquo;在人民陪审员选任上，必须综合考虑多方面因素，既要履行好审判员的角色，还要发挥好监督员、调解员、宣传员、咨询员的作用。&rdquo;漳州市中级人民法院院长吴钟夏多次在会上强调。记者获悉，近年来，漳州法院在陪审员选任方面始终坚持&ldquo;五员&rdquo;定位，陈家富就是&ldquo;五员&rdquo;的代表之一。</span><br />\n" +
                "<br />\n" +
                "<span style=\"background-color:rgb(255,255,255); color:rgb(36,36,36)\">　　漳州在全市每个乡镇、街道至少选任3名陪审员，实现全覆盖。5年来，全市731名人民陪审员积极参与婚姻家庭邻里纠纷调解、涉诉信访案件化解、协助查找被执行人和财产下落、反映社情民意、积极开展普法宣传，在净化纠纷、弘扬法治上发挥着生力军的作用。</span><br />\n" +
                "<br />\n" +
                "<span style=\"background-color:rgb(255,255,255); color:rgb(36,36,36)\">　　特色参审 发挥专长</span><br />\n" +
                "<br />\n" +
                "<span style=\"background-color:rgb(255,255,255); color:rgb(36,36,36)\">　　漳州法院按照&ldquo;集中管理、分类使用&rdquo;的原则，不但让人民陪审员在具体个案审理中发挥重要的专业评判功能，也让他们成为行业领域内的&ldquo;法治辅导员&rdquo;。</span><br />\n" +
                "<br />\n" +
                "<span style=\"background-color:rgb(255,255,255); color:rgb(36,36,36)\">　　&ldquo;请问原告，你们养殖鳗鱼的水源哪里来？苗种来自哪里？养殖模式是什么？用什么饲料？发现鳗鱼死亡的时候，水质情况如何？&hellip;&hellip;&rdquo;审判席上，人民陪审员陈永有连续向原告抛出12个问题。</span><br />\n" +
                "<br />\n" +
                "<span style=\"background-color:rgb(255,255,255); color:rgb(36,36,36)\">　　因饲养的鳗鱼在海堤扩建期间大量死亡，原告向法院提起侵权之诉。</span><br />\n" +
                "<br />\n" +
                "<span style=\"background-color:rgb(255,255,255); color:rgb(36,36,36)\">　　作为这一领域的专家，同时也是漳浦县海洋渔业局的高级工程师，陈永有的专业知识不容小觑。考虑到案件事实证据评判的专业性，法院通知其作为专家陪审员参加案件审理。</span><br />\n" +
                "<br />\n" +
                "<span style=\"background-color:rgb(255,255,255); color:rgb(36,36,36)\">　　庭审中，陈永有发挥了其独有的专业优势，并在合议庭案件评议时提出专业评判意见。最终，漳州中院以证据不足为由驳回原告的诉讼请求。</span><br />\n" +
                "<br />\n" +
                "<span style=\"background-color:rgb(255,255,255); color:rgb(36,36,36)\">　　60多岁的洪亚池，也被称为&ldquo;护林陪审员&rdquo;,他先后成功调处了涉九龙岭国有林场、林下国有林场等26起山林纠纷，参加审理的民商事案件调解撤诉率高达88.5%。</span><br />\n" +
                "<br />\n" +
                "<span style=\"background-color:rgb(255,255,255); color:rgb(36,36,36)\">　　&ldquo;前几年，林场与村民之间发生的矛盾比较频繁，经常发生盗伐、环割、套种等破坏现象，我因为有处纠办、林改办的工作经验，与群众打交道多，处理涉林纠纷更有针对性。&rdquo;洪亚池告诉记者。</span><br />\n" +
                "<br />\n" +
                "<span style=\"background-color:rgb(255,255,255); color:rgb(36,36,36)\">　　去年，张某雇请车辆、工人先后两次盗伐柠檬桉。在参审该案中，鉴于当地毁坏山林较为严重的现状，洪亚池提出了&ldquo;从快从重打击&rdquo;的意见，该案判决后在当地发挥了警示作用，涉林纠纷大量减少。</span><br />\n" +
                "<br />\n" +
                "<span style=\"background-color:rgb(255,255,255); color:rgb(36,36,36)\">　　记者了解到，除了陈永有、洪亚池等21名生态专家陪审员外，漳州法院还选任了7名军人军属担任人民陪审员。</span><br />\n" +
                "<br />\n" +
                "<span style=\"background-color:rgb(255,255,255); color:rgb(36,36,36)\">　　规范保障 个性考评</span><br />\n" +
                "<br />\n" +
                "<span style=\"background-color:rgb(255,255,255); color:rgb(36,36,36)\">　　&ldquo;庭审时，人民陪审员黄丽琼能认真判断认定案件事实；评议时，能就案件事实及案件处理独立发表意见。&rdquo;今年4月22日，平和县人民法院主审法官赖丰富在一份《人民陪审员参审流程管理信息表》填上自己对人民陪审员的参审评价。</span><br />\n" +
                "<br />\n" +
                "<span style=\"background-color:rgb(255,255,255); color:rgb(36,36,36)\">　　&ldquo;一案一表&rdquo;，成为人民陪审员工作业绩的实时记录仪。在参审个案后，审判长将就陪审员的参审能力、庭审作风等方面进行客观评价，并将综合情况计入个人业绩档案、廉政档案，作为年终考核依据。</span><br />\n" +
                "<br />\n" +
                "<span style=\"background-color:rgb(255,255,255); color:rgb(36,36,36)\">　　为解决&ldquo;陪而不审&rdquo;、&ldquo;审而不议&rdquo;等问题，漳州法院严格落实庭前联系、准备会制度，定期或不定期开展庭审和案卷抽查、评查，随时了解人民陪审员审理案件、发表意见、独立表决的情况。</span><br />\n" +
                "<br />\n" +
                "<span style=\"background-color:rgb(255,255,255); color:rgb(36,36,36)\">　　今年1月，龙海市人民法院还自主研发陪审员管理软件，将资料登记、参审抽选、开庭短信通知与绩效考核等功能进行整合。此外，该院还严格执行人民陪审员退出机制，去年共对6名不能胜任的人民陪审员提请同级人大常委会予以免职。</span><br />\n" +
                "<br />\n" +
                "<span style=\"background-color:rgb(255,255,255); color:rgb(36,36,36)\">　　&ldquo;让陪审员在履职中有一种荣誉感，重要的是要保障他们参审的权利，包括出庭权、调查权、评议权、调解权，甚至是列席审委会的权利等。&rdquo;漳州中院政治部副主任张志凌介绍说。</span><br />\n" +
                "<br />\n" +
                "<span style=\"background-color:rgb(255,255,255); color:rgb(36,36,36)\">　　此外，在保障陪审员履职方面，漳州90%的基层法院设立了人民陪审员专项经费，实行办案、交通、食宿补贴制度，还专门设立陪审员办公室、休息室，配备正装、工作牌等。</span>&nbsp;</span></p>\n";
                webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.loadData(source, "text/html; charset=UTF-8", null);
        webView.setWebViewClient(new WebViewClient());
    }

    @OnClick(R.id.itv_thumb)
    public void thumb() {

        itvThumb.setTextColor(ColorOverrider.getInstance(this).getColorAccent());
        ToastUtils.show(this, "已赞");
    }

    boolean star = false;

    @OnClick(R.id.itv_star)
    public void onStarClick() {

        if (star) {
            unStar();
            star = false;
        } else {
            star();
            star = true;
        }
    }

    public void star() {

        itvStar.setTextColor(ColorOverrider.getInstance(this).getColorAccent());
        ToastUtils.show(this, "已关注");
    }

    public void unStar() {

        itvStar.setTextColor(Color.parseColor("#cccccc"));
        ToastUtils.show(this, "已取消关注");
    }


    @OnClick(R.id.itv_comment)
    public void comment() {

        startActivity(CommentActivity.class);
    }

    @OnClick(R.id.add_comment)
    public void startAddCommentActivity() {

        startActivity(AddCommentActivity.class);
    }

}

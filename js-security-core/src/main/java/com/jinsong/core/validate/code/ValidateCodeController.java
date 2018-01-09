package com.jinsong.core.validate.code;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import com.jinsong.core.properties.SecurityProperties;

@RestController
public class ValidateCodeController {
	
	public static final String SESSION_KEY="SESSION_KEY_IMAGE_CODE";
	
	public static final String SESSION_KEY_SMS="SESSION_KEY_SMS_CODE";
	
	private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
	
	@Autowired
	private SecurityProperties securityProperties;
	
	@Autowired
	private SmsCodeSender smsCodeSender;

	//调用这个URL就是生成验证码图片
	@GetMapping("/code/image")
	public void createCode(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		//生成验证码图片
		ImageCode imageCode =createImageCode(new ServletWebRequest(request));
		
		//将imageCode存入session中，名字是SESSION_KEY="SESSION_KEY_IMAGE_CODE"
		sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY, imageCode);
		
		//将生成的图片写到接口的响应中
		ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
	}
	
	//短信验证码
	@GetMapping("/code/sms") //@ResponseBody	//加不加@ResponseBody 都返回的JSON格式的数据啊！！！！！！！！！！！！
	public MobileTest createSmsCode(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletRequestBindingException {
		
		//生成验证码图片
		SmsCode smsCode =createSmsCode(new ServletWebRequest(request));
		
		//将SmsCode存入session中，名字是SESSION_KEY_SMS="SESSION_KEY_SMS_CODE"
		sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY_SMS, smsCode);
		
		//用短信服务商发送短信验证码到用户手机上
		//用了飞鸽传书公司的服务，经过测试，是OK的，具体可以看OneNote笔记
		//这里就不写具体服务了，只模拟一个手机号mobile和验证码smsCode的值出来
		String mobile = ServletRequestUtils.getRequiredStringParameter(request, "mobile"); 
		smsCodeSender.send(mobile, smsCode.getCode());
		return new MobileTest(mobile, smsCode.getCode());	//返回这个类，就会返回JSON格式的数据，比如这样{"mobile":"18571686931","code":"316375"}
	}

	private SmsCode createSmsCode(ServletWebRequest servletWebRequest) {
		String code = RandomStringUtils.randomNumeric(6);	//随机一个6位验证码
		return new SmsCode(code, 60);//短信验证码过期时间60秒
	}

	//生成图形验证码图片的具体逻辑
	private ImageCode createImageCode(ServletWebRequest request) {
		//图片的宽高
		//默认值是securityProperties中的默认值，如果HTTP请求中有width参数，则会覆盖掉这个默认值
		//即HTTP请求级配置会覆盖应用级配置会覆盖默认配置
		int width = ServletRequestUtils.getIntParameter(request.getRequest(), "width", securityProperties.getImage().getWidth());
		int height = ServletRequestUtils.getIntParameter(request.getRequest(), "height", securityProperties.getImage().getHeight());
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics g = image.getGraphics();

		Random random = new Random();

		//设置一些干扰的条纹
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}

		String sRand = "";
		//for循环几次就是几位验证码
		for (int i = 0; i < securityProperties.getImage().getLength(); i++) {	
			String rand = String.valueOf(random.nextInt(10));
			sRand += rand;
			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(rand, 13 * i + 6, 16);
		}

		g.dispose();

		return new ImageCode(image, sRand, securityProperties.getImage().getExpireIn());//第三个参数是过期时间，默认60秒
	}
	
	/**
	 * 生成随机背景条纹
	 * 
	 * @param fc
	 * @param bc
	 * @return
	 */
	private Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255) {
			fc = 255;
		}
		if (bc > 255) {
			bc = 255;
		}
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
}

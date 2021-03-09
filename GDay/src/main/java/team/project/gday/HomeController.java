package team.project.gday;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import team.project.gday.Product.model.vo.Attachment;
import team.project.gday.Product.model.vo.GClass;
import team.project.gday.Product.model.vo.ProductStar;
import team.project.gday.gClass.model.service.GClassService;
import team.project.gday.gift.model.service.GiftService;
import team.project.gday.gift.model.vo.Gift;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private GClassService cService;
	private GiftService gService;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		List<GClass> cList = cService.selectClassList3();
			if(cList != null && !cList.isEmpty()) {
				List<Attachment> thList = cService.selectThumbnailList(cList);
				System.out.println(thList);
				model.addAttribute("thList", thList);
				
				//평균 별점 가져오기
				List<ProductStar> selectStarList = cService.selectStarList(cList);
				System.out.println(selectStarList);
				model.addAttribute("selectStarList", selectStarList);
			}
		//List<Gift> selectGiftList3 = gService.selectGiftList3();
		model.addAttribute("cList", cList);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "common/main";
	}
	
}

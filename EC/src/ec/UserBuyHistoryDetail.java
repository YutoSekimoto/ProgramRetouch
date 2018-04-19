package ec;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.BuyDataBeans;
import beans.DeliveryMethodDataBeans;
import beans.ItemDataBeans;
import dao.BuyDAO;
import dao.DeliveryMethodDAO;
import dao.ItemDAO;

/**
 * 購入履歴画面
 * @author d-yamaguchi
 *
 */
@WebServlet("/UserBuyHistoryDetail")
public class UserBuyHistoryDetail extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

		HttpSession session = request.getSession();
		try {

			String buyId = request.getParameter("buy_id");
			int intBuyId = Integer.parseInt(buyId);

			//購入詳細取得
			BuyDAO buyDao = new BuyDAO();
			BuyDataBeans buyDataBeans = buyDao.getBuyDataBeansByBuyId(intBuyId);

			//配送方法取得
			int ID = buyDataBeans.getDelivertMethodId();
			//選択されたIDをもとに配送方法Beansを取得
			DeliveryMethodDataBeans userSelectDMB = DeliveryMethodDAO.getDeliveryMethodDataBeansByID(ID);
			String deliverName = userSelectDMB.getName();
			//配送方法の名前をセット
			buyDataBeans.setDeliveryMethodName(deliverName);


			//購入IDからアイテムIDを取得
			String itemId = buyDao.getBuyId(intBuyId);
			String[] allId = itemId.split(",");


			//全てのアイテム情報を取得
			ArrayList<ItemDataBeans> itemAll = new ArrayList<ItemDataBeans>();
			ItemDAO itemDao = new ItemDAO();

			for(String sItemId : allId) {


				int iItemId = Integer.parseInt(sItemId);
				ItemDataBeans itemDateBeans = new ItemDataBeans();

				out.println(iItemId);

				itemDateBeans = itemDao.getItemByItemID(iItemId);

				itemAll.add(itemDateBeans);

			}


			request.setAttribute("itemAll", itemAll);
			request.setAttribute("buyDataBeans", buyDataBeans);

			request.getRequestDispatcher(EcHelper.USER_BUY_HISTORY_DETAIL_PAGE).forward(request, response);


		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("errorMessage", e.toString());
			response.sendRedirect("Error");
		}

	}
}

package web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.connector.Response;

import java.sql.PreparedStatement;

import dao.CategorieDaoImpl;
import dao.ICategorieDao;
import dao.IProduitDao;
import dao.ProduitDaoImpl;
import metier.Categorie;
import metier.Produit;
@WebServlet (name="cs",urlPatterns= {"/controleur","*.do"})
public class ControleurServlet extends HttpServlet {
	IProduitDao metier;
	ICategorieDao metierCat;
	@Override
	public void init() throws ServletException {
	metier = new ProduitDaoImpl();
	metierCat = new CategorieDaoImpl();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
			String path=request.getServletPath();
			if (path.equals("/index.do")) { 
			List<Categorie> cats = metierCat.getAllCategories();
			CategorieModele catModel= new CategorieModele();
			catModel.setCategories(cats);
			request.setAttribute("catModel", catModel);
			request.getRequestDispatcher("produits.jsp").forward(request,response);
			}
			else if (path.equals("/recherche.do")) { 
				List<Categorie> cats = metierCat.getAllCategories();
				CategorieModele catModel= new CategorieModele();
				catModel.setCategories(cats);
				request.setAttribute("catModel", catModel);
				request.getRequestDispatcher("recherche.jsp").forward(request,response);
				}
				
			
			else if (path.equals("/chercher.do")){
				String motCle=request.getParameter("motCle");
				ProduitModele model= new ProduitModele();
				model.setMotCle(motCle);
				List<Categorie> cats = metierCat.getAllCategories();
				CategorieModele catModel= new CategorieModele();
				catModel.setCategories(cats);
				List<Produit> prods = metier.produitsParMC(motCle);
				model.setProduits(prods);
				request.setAttribute("catModel", catModel);
				request.setAttribute("model", model);
				request.getRequestDispatcher("produits.jsp").forward(request,response);
			} 
			else if (path.equals("/chercher_cat.do")){
				List<Categorie> cats = metierCat.getAllCategories();
				CategorieModele catModel= new CategorieModele();
				catModel.setCategories(cats);
				Long idCat = Long.parseLong(request.getParameter("categorie"));
				ProduitModele model = new ProduitModele();
				model.setIdCat(idCat);
				List<Produit> prods = metier.produitsParCat(idCat);
				model.setProduits(prods);
				request.setAttribute("catModel", catModel);
				request.setAttribute("model", model);
				request.setAttribute("selectedId",idCat);
				request.getRequestDispatcher("recherche.jsp").forward(request,response);
			}
			 
			else if (path.equals("/saisie.do") )
			{
			List<Categorie> cats = metierCat.getAllCategories();
			CategorieModele model= new CategorieModele();
			model.setCategories(cats);
			request.setAttribute("catModel", model);
			request.getRequestDispatcher("saisieProduit.jsp").forward(request,response);
			}

	
			else if (path.equals("/save.do") && request.getMethod().equals("POST"))
			{
			String nom=request.getParameter("nom");
			Long categorieId=Long.parseLong(request.getParameter("categorie"));
			 double prix = Double.parseDouble(request.getParameter("prix"));
			 Categorie cat = metierCat.getCategorie(categorieId); 
			Produit p = metier.save(new Produit(nom,prix,cat));
			request.setAttribute("produit", p);
			response.sendRedirect("chercher.do?motCle="); }

			
			else if (path.equals("/supprimer.do")){
			 Long id= Long.parseLong(request.getParameter("id"));
			 metier.deleteProduit(id);
			 response.sendRedirect("chercher.do?motCle=");
			}
			
			else if (path.equals("/editer.do") )
			{
			 Long id= Long.parseLong(request.getParameter("id"));
			 Produit p = metier.getProduit(id);
			 request.setAttribute("produit", p);
			 
			 List<Categorie> cats = metierCat.getAllCategories();
			 CategorieModele model= new CategorieModele();
			 model.setCategories(cats);
			 request.setAttribute("catModel", model);
			request.getRequestDispatcher("editerProduit.jsp").forward(request,response);
			}

			
			else if (path.equals("/update.do") )
			{ Long id = Long.parseLong(request.getParameter("id"));
			String nom=request.getParameter("nom");
			double prix = Double.parseDouble(request.getParameter("prix"));
			Long categorieId=Long.parseLong(request.getParameter("categorie"));
			Produit p = new Produit();
			p.setIdProduit(id);
			p.setNomProduit(nom);
			p.setPrix(prix);
			Categorie cat = metierCat.getCategorie(categorieId);
			p.setCategorie(cat);
			metier.updateProduit(p);
			response.sendRedirect("chercher.do?motCle=");
			}
 
			
			else response.sendError(Response.SC_NOT_FOUND);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
		ServletException, IOException {
			doGet(request,response);
	}
	
	public Produit getProduit(Long id) {
		Connection conn=SingletonConnection.getConnection();
		Produit p = new Produit();
		 try {
				PreparedStatement ps= conn.prepareStatement("select * from PRODUITS where ID_PRODUIT = ?");
				ps.setLong(1, id);
				ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				p.setIdProduit(rs.getLong("ID_PRODUIT"));
				p.setNomProduit(rs.getString("NOM_PRODUIT"));
				p.setPrix(rs.getDouble("PRIX"));
			}
		 } catch (SQLException e) {
			 e.printStackTrace();
		 }
		return p;
	}
	
	public Produit updateProduit(Produit p) {
		Connection conn=SingletonConnection.getConnection();
		 try {
			PreparedStatement ps= conn.prepareStatement("UPDATE PRODUITS SET NOM_PRODUIT=?,PRIX=? WHERE ID_PRODUIT=?");
			ps.setString(1, p.getNomProduit());
			ps.setDouble(2, p.getPrix());
			ps.setLong(3, p.getIdProduit());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return p;
	}
	
	public void deleteProduit(Long id) {
		Connection conn=SingletonConnection.getConnection();
		 try {
			PreparedStatement ps= conn.prepareStatement("DELETE FROM PRODUITS WHERE ID_PRODUIT = ?");
			ps.setLong(1, id);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
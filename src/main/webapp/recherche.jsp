<%@ page language="java" contentType="text/html; charset=windows-1256"
 pageEncoding="windows-1256"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1256">
	<title>Insert title here</title>
	<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css" />
</head>

<body>
	<%@include file="header.jsp" %>
	<p></p>
	<div class="container">
		<div class="card">
	 		<div class="card-header">
	 			Recherche des Produits
	 		</div>
	 		<div class="card-body">
				 <form action="chercher_cat.do" method="get">
              <table> <tr><td>Liste des catégories</td>
              <td><select name="categorie" >
              <c:forEach items="${catModel.categories}" var="cat"> 
              <c:choose>
              <c:when test="${cat.idCat == selectedId}">
              <option value="${cat.idCat}" selected>${cat.nomCat}</option>
              </c:when>
              <c:otherwise>
              <option value="${cat.idCat}">${cat.nomCat}</option>
              </c:otherwise>
              </c:choose>
              </c:forEach>
              </select></td>
 <td><button type="submit" class="btn btn-primary">Rechercher</button></tr>
 <!--  <tr><td>selected id is : ${selectedId}</td></tr> -->
 </table>
  </form>
  <br>
				 <table class="table table-striped">
					 <tr>
					 	<th>ID</th>
					 	<th>Nom Produit</th>
					 	<th>Prix</th>
					 	<th>Categorie</th>
					 	<th>Supprimer</th>
					 	<th>Modifier</th>
					 </tr>
					 <c:forEach items="${model.produits}" var="p">
						 <tr>
							 <td>${p.idProduit }</td>
							 <td>${p.nomProduit }</td>
							 <td>${p.prix }</td>
							 <td>${p.categorie.nomCat }</td>
							 <td>
							 	<a onclick="return confirm('Etes-vous sûr ?')" href="supprimer.do?id=${p.idProduit }">Supprimer</a>
							 </td>
	 						 <td>
	 						 	<a href="editer.do?id=${p.idProduit }">Edit</a>
	 						 </td>
						 </tr>
		 			</c:forEach>
	 			</table>
	 		</div>
		</div>
	</div>
</body>

</html>
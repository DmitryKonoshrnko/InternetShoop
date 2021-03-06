<%@ page contentType="text/html; UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<div class="container">
    <div class="row">
        <c:if test="${not empty message}">
            <div class="col-xs-12">
                    <div class="alert alert-danger">
                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                            ${message}
                    </div>
            </div>
        </c:if>
        <div class="form-container1122">
            <div class="panel panel-primary">
                <div class="form-title1122">
                    <h4>Редактирование товаров</h4>
                </div>
                <div class="panel-body">
                    <!-- Form Elements -->
                    <%--@elvariable id="product" type="com.divanxan.internetshop.dto.Product"--%>
                    <sf:form class="form-horizontal" modelAttribute="product"
                             action="${contextRoot}/manage/product"
                             method="POST"
                             enctype="multipart/form-data"
                    >
                    <div class="form-group">
                        <label class="form-title1122" for="name">Введите название товара</label>
                        <div class="col-md-8">
                            <sf:input type="text" path="name" id="name" placeholder="Название товара"
                                      class="form-field1122"/>
                            <sf:errors path="name" cssClass="help-block" element="em"/>
                        </div>
                        <div class="form-group">
                            <label class="form-title1122" for="brand">Введите название бренда</label>
                            <div class="col-md-8">
                                <sf:input type="text" path="brand" id="brand" placeholder="Название бренда"
                                          class="form-field1122"/>
                                <sf:errors path="brand" cssClass="help-block" element="em"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="form-title1122" for="description">Введите описание товара</label>
                            <div class="col-md-8">
                                <sf:textarea path="description" id="description" rows="4"
                                             placeholder="Не то, чтобы обязательно писать... Можно скопировать"
                                             class="form-field1122"/>
                                <sf:errors path="description" cssClass="help-block" element="em"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="form-title1122" for="quantity">Введите количество</label>
                            <div class="col-md-8">
                                <sf:input type="number" min="0" max="50000" path="quantity" id="quantity"
                                          placeholder="Доступное количество товара"
                                          class="form-field1122"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="form-title1122" for="unitPrice">Введите цену товара</label>
                            <div class="col-md-8">
                                <sf:input type="number" min="1" max="999999" path="unitPrice" id="unitPrice"
                                          placeholder="Цена в рублях" class="form-field1122"/>
                                <sf:errors path="unitPrice" cssClass="help-block" element="em"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="form-title1122" for="quantity">Введите id сопутсвующего товара</label>
                            <div class="col-md-8">
                                <sf:input type="" path="productDisId" id="productDisId"
                                          class="form-field1122"/>
                                <sf:errors path="productDisId" cssClass="help-block" element="em"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="form-title1122" for="discount">Введите скидку сопутсвующего товара</label>
                            <div class="col-md-8">
                                <sf:input type="number" min="0" max="15" path="discount" id="discount"
                                          class="form-field1122"/>
                                <sf:errors path="productDisId" cssClass="help-block" element="em"/>
                            </div>
                        </div>
                        <!-- File for image upload-->
                        <div class="form-group">
                            <label class="form-title1122" for="file">Загрузите изображение товара</label>
                            <div class="col-md-8">
                                <sf:input type="file" path="file" id="file" class="form-field1122"/>
                                <sf:errors path="file" cssClass="help-block" element="em"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="form-title1122" for="categoryId">Выбирете категорию: </label>
                            <div class="col-md-8">
                                <sf:select class="form-field1122" id="categoryId" path="categoryId"
                                           items="${categories}"
                                           itemLabel="name"
                                           itemValue="id"
                                />
                                <c:if test="${product.id ==0}">
                                    <div class="text-right">
                                        <br/>
                                        <button type="button" data-toggle="modal" data-target="#myCategoryModal"
                                                class="submit-button1122">Добавьте категорию
                                        </button>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-md-offset-4 col-md-8">
                                <input type="submit" name="submit" id="submit" value="Сохранить"
                                       class="submit-button1122"/>
                                <!-- Hidden fields for products -->
                                <sf:hidden path="id"/>
                                <sf:hidden path="code"/>
                                <sf:hidden path="supplierId"/>
                                <sf:hidden path="active"/>
                                <sf:hidden path="purchases"/>
                                <sf:hidden path="views"/>
                            </div>
                        </div>
                        </sf:form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="form-container">
            <div class="form-title1122g">
                <h3>Товары:</h3>
            </div>
                <div class="container-fluid">
                    <div class="table-responsive">
                        <table id="adminProductsTable" class="table table-striped table-bordered">
                            <thead>
                            <tr>
                                <th>Id</th>
                                <th>&#160</th>
                                <th>Название</th>
                                <th>Производитель</th>
                                <th>Количество</th>
                                <th>&#8381; Цена</th>
                                <th>ID товара</th>
                                <th>Скидка %</th>
                                <th>Активность</th>
                                <th>Редактировать</th>
                            </tr>
                            </thead>
                            <tfoot>
                            <tr>
                                <th>Id</th>
                                <th>&#160</th>
                                <th>Название</th>
                                <th>Производитель</th>
                                <th>Количество</th>
                                <th>&#8381; Цена</th>
                                <th>ID товара</th>
                                <th>Скидка %</th>
                                <th>Активность</th>
                                <th>Редактировать</th>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
        </div>
    </div>
    <!-- Modal form -->
    <div class="modal fade" id="myCategoryModal" role="dialog" tabindex="-1">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <!-- Modal Header -->
                <div class="modal-header">
                    <h4 class="modal-title"> Добавьте новую категорию</h4>
                    <button type="button" class="close" data-dismiss="modal">
                        <span>&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <!-- Category Form -->
                    <%--@elvariable id="category" type="com.divanxan.internetshop.dto.Category"--%>
                    <sf:form id="categoryForm" modelAttribute="category" action="${contextRoot}/manage/category" method="POST"
                             cssClass="form-horizontal">
                        <div class="form-group">
                            <label for="category_name" class="control-label col-md-4">Название категории</label>
                            <div class="col-md-8">
                                <sf:input type="text" path="name" id="category_name" cssClass="form-control"/>
                                <sf:errors path="name" cssClass="help-block" element="em"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="category_description" class="control-label col-md-4">Описание категории</label>
                            <div class="col-md-8">
                                <sf:textarea cols="" rows="5" path="description" id="category_description"
                                             cssClass="form-control"/>
                                <sf:errors path="description" cssClass="help-block" element="em"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-md-offset-4 col-md-8">
                                <input type="submit" value="Добавить катеорию" class="submit-button1122"/>
                            </div>
                        </div>
                    </sf:form>
                </div>
            </div>
        </div>
    </div>
</div>
<%--
- form.jsp
-
- Copyright (C) 2012-2021 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<acme:form>
	<jstl:if test="${command!='create'}">
		<acme:form-moment code="anonymous.shout.form.label.moment" path="moment" readonly="true"/>
		<acme:form-textbox code="anonymous.shout.form.label.author" path="author" readonly="true"/>
		<acme:form-textarea code="anonymous.shout.form.label.text" path="text" readonly="true"/>
		<acme:form-url code="anonymous.shout.form.label.info" path="info" readonly="true"/>
		<br>
		<br>
		<h3><acme:message code="anonymous.shout.form.patata"/></h3>
		<br>
		<acme:form-textbox code="anonymous.shout.form.label.patataTicker" path="patataTicker" readonly="true"/>
		<acme:form-moment code="anonymous.shout.form.label.patataMoment" path="patataMoment" readonly="true"/>
		<acme:form-money code="anonymous.shout.form.label.patataValue" path="patataValue" readonly="true"/>
		<acme:form-checkbox code="anonymous.shout.form.label.patataBoolean" path="patataBoolean" readonly="true"/>
	</jstl:if>
	<jstl:if test="${command=='create' }">
		<acme:form-textbox code="anonymous.shout.form.label.author" path="author"/>
		<acme:form-textarea code="anonymous.shout.form.label.text" path="text"/>
		<acme:form-url code="anonymous.shout.form.label.info" path="info"/>
		<br>
		<br>
		<h3><acme:message code="anonymous.shout.form.patata"/></h3>
		<br>
		<acme:form-textbox code="anonymous.shout.form.label.patataTicker" path="patataTicker"/>
		<acme:form-moment code="anonymous.shout.form.label.patataMoment" path="patataMoment"/>
		<acme:form-money code="anonymous.shout.form.label.patataValue" path="patataValue"/>
		<acme:form-checkbox code="anonymous.shout.form.label.patataBoolean" path="patataBoolean"/>
		
	</jstl:if>
	<acme:form-submit test="${command=='create'}" code="anonymous.shout.form.button.create" action="/anonymous/shout/create"/>
	<acme:form-return code="anonymous.shout.form.button.return"/>
</acme:form>

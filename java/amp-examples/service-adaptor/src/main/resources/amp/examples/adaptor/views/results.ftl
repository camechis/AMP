<#-- @ftlvariable name="" type="amp.examples.notifier.views.MailboxView" -->
<!DOCTYPE html>
<html lang="en-US">
	<header>
		<title>Get Queue Results</title>
		<script type="text/javascript" src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
	</header>
	<body>
		<h1>Queued Results</h1>
		<form action="/service/results" method="DELETE">
			<button type="submit">Clear Queue</button>
		</form>
		<table>
			<tr>
				<th>Headers</th>
				<th>Payload</th>
			</tr>
			<#list results as result>
				<tr>
					<td>${result.formattedHeaders}</td>
					<td>${result.stringPayload}</td>
				</tr>
			</#list>
		</table>
	</body>
</html>
<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:ieso="http://www.ieso.ca/schema" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:template match="/">
		<xsl:variable name="createdAt">
			<xsl:value-of select="ieso:Document/ieso:DocHeader/ieso:CreatedAt"/>
		</xsl:variable>
		<xsl:variable name="deliveryDate">
			<xsl:value-of select="ieso:Document/ieso:DocBody/ieso:DeliveryDate"/>
		</xsl:variable>
		<xsl:variable name="deliveryHour">
			<xsl:value-of select="ieso:Document/ieso:DocBody/ieso:DeliveryHour"/>
		</xsl:variable>
		<xsl:variable name="smallcase" select="'abcdefghijklmnopqrstuvwxyz'"/>
		<xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'"/>
		<html>
			<head>
				<script type="text/javascript"> 	
					var cX = 0; var cY = 0;
					function updateCursorPosition(e)
					{ 
						cX = e.pageX; cY = e.pageY;
					}
					function updateCursorPositionDocAll()
					{
						var de = document.documentElement;
						var b = document.body;
						cX = event.clientX + 
							(de.scrollLeft || b.scrollLeft) - (de.clientLeft || 0);
						cY = event.clientY + 
							(de.scrollTop || b.scrollTop) - (de.clientTop || 0);
					}					
					if(document.all) 
					{
						document.onmousemove = updateCursorPositionDocAll; 
					}
					else 
					{
						document.onmousemove = updateCursorPosition; 
					}				
					function assignPosition(d) 
					{
						d.style.left = (cX -20) + "px";
						d.style.top = (cY -40) + "px";
					}				
					function hideHelp(d) 
					{
						document.getElementById(d).style.display = "none";
					}				
					function makeItVisible(d) 
					{
						var dd = document.getElementById(d);
						assignPosition(dd);
						dd.style.display = "block";
					}									
				</script>
				<title>
					<xsl:value-of select="ieso:Document/ieso:DocHeader/ieso:DocTitle"/>
				</title>
				<link rel="stylesheet" type="text/css" href="http://www.ieso.ca/imoweb/xml/adcs.css"/>
			</head>
			<body>
				<div class="reportHeader">
					<div class="balloon" id="Title" style="display:none; width:300px; height:35px; ">
						The purpose of this report is to provides Realtime Constrained Totals for the current hour.
					</div>
					<div class="balloon" id="Date" style="display:none; width:200px; height:35px;">
						The date and delivery hour for which this data applies
					</div>
					<div class="balloon" id="CreationDate" style="display:none; width:150px; height:35px;">
						Report creation date and time in EST
					</div>
					<div class="balloon" id="EnergyMW" style="display:none; width:200px; height:35px;">
						Amount of Energy Scheduled for the delivery hour
					</div>
					<div class="balloon" id="Interval" style="display:none; width:200px; height:35px;">
						The Interval for which this data applies (1 to 12)
					</div>
					<div class="balloon" id="TotEnergy" style="display:none; width:100px; height:20px;">
						Total Energy
					</div>
					<div class="balloon" id="TotSpin" style="display:none; width:150px; height:35px;">
						Total 10 Minute Spinning Reserve
					</div>
					<div class="balloon" id="TotNonSpin" style="display:none; width:150px; height:35px;">
						Total 10 Minute Non Spinning Reserve
					</div>
					<div class="balloon" id="TotResv" style="display:none; width:150px; height:20px;">
						Total 30 Minute Reserve
					</div>
					<div class="balloon" id="TotDispLoad" style="display:none; width:150px; height:20px;">
						Total Dispatchable Load
					</div>
					<div class="balloon" id="TotLoad" style="display:none; width:100px; height:20px;">
						Total Load
					</div>
					<div class="balloon" id="TotLoss" style="display:none; width:100px; height:20px;">
						Total Losses
					</div>
					<div class="balloon" id="OntDemand" style="display:none; width:100px; height:20px;">
						Ontario Demand
					</div>
					<br/>
					<table width="100%">
						<tbody>
							<tr>
								<td align="left" width="25%" valign="top">
									<h1 align="center">
										<IMG alt="" src="https://reports.ieso.ca/images/logo.gif"/>
									</h1>
								</td>
								<td width="50%">
									<h1 align="center">
										<a class="isTip" onmouseover="makeItVisible('Title')" onmouseout="hideHelp('Title')" href="#">
											<xsl:value-of select="ieso:Document/ieso:DocHeader/ieso:DocTitle"/>
										</a>
									</h1>
									<h2 align="center">
										<table class="subtable" align="center">
											<tr>
												<td align="right">
													<a class="isTip" onmouseover="makeItVisible('CreationDate')" onmouseout="hideHelp('CreationDate')" href="#">Created at </a>
												</td>
												<td align="left">
													<xsl:call-template name="Month">
														<xsl:with-param name="month">
															<xsl:value-of select="substring($createdAt, 6, 2)"/>
														</xsl:with-param>
													</xsl:call-template>
													<xsl:text> </xsl:text>
													<xsl:value-of select="substring($createdAt, 9, 2)"/>
													<xsl:text>, </xsl:text>
													<xsl:value-of select="substring($createdAt, 1, 4) "/>
													<xsl:text> </xsl:text>
													<xsl:value-of select="substring($createdAt, 12) "/>
													<xsl:text> </xsl:text>
												</td>
											</tr>
											<tr>
												<td align="right">
													<a class="isTip" onmouseover="makeItVisible('Date')" onmouseout="hideHelp('Date')" href="#">For </a>
												</td>
												<td align="left">
													<xsl:call-template name="Month">
														<xsl:with-param name="month">
															<xsl:value-of select="substring($deliveryDate, 6, 2)"/>
														</xsl:with-param>
													</xsl:call-template>
													<xsl:text> </xsl:text>
													<xsl:value-of select="substring($deliveryDate, 9, 2)"/>
													<xsl:text>, </xsl:text>
													<xsl:value-of select="substring($deliveryDate, 1, 4) "/>
													<xsl:text> </xsl:text>
													<xsl:value-of select="substring($deliveryDate, 12) "/>
													<xsl:text> </xsl:text>
													<xsl:text> - Hour </xsl:text>
													<xsl:value-of select="$deliveryHour"/>
												</td>
											</tr>
										</table>
									</h2>
								</td>
								<td width="50%" align="right" valign="top">
									<a href="/docrefs/helpfile/RealtimeConstTotals_h2.pdf">
										<b>
											<i>Help</i>
										</b>
									</a>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				<br/>
				<div class="report">
					<xsl:for-each select="ieso:Document/ieso:DocBody/ieso:Energies">
						<table border="1" bordercolor="#000000" cellspacing="0" cellpadding="0" bgcolor="white" width="600" align="center">
							<tr>
								<td class="c1headc1" width="7%" rowspan="2">
									<div align="center">
										<a class="isTip" onmouseover="makeItVisible('Interval')" onmouseout="hideHelp('Interval')" href="#">Interval</a>
									</div>
								</td>
								<td class="c1headc1" width="90%" colspan="8">
									<div align="center">
										<a class="isTip" onmouseover="makeItVisible('EnergyMW')" onmouseout="hideHelp('EnergyMW')" href="#">MW</a>
									</div>
								</td>
							</tr>
							<tr>
								<td class="c1headc1" width="11%">
									<div align="center">
										<a class="isTip" onmouseover="makeItVisible('TotEnergy')" onmouseout="hideHelp('TotEnergy')" href="#">TOTAL ENERGY</a>
									</div>
								</td>
								<td class="c1headc1" width="11%">
									<div align="center">
										<a class="isTip" onmouseover="makeItVisible('TotLoss')" onmouseout="hideHelp('TotLoss')" href="#">TOTAL LOSS</a>
									</div>
								</td>
								<td class="c1headc1" width="11%">
									<div align="center">
										<a class="isTip" onmouseover="makeItVisible('TotLoad')" onmouseout="hideHelp('TotLoad')" href="#">TOTAL LOAD</a>
									</div>
								</td>
								<td class="c1headc1" width="11%">
									<div align="center">
										<a class="isTip" onmouseover="makeItVisible('TotDispLoad')" onmouseout="hideHelp('TotDispLoad')" href="#">TOTAL DISP LOAD</a>
									</div>
								</td>
								<td class="c1headc1" width="11%">
									<div align="center">
										<a class="isTip" onmouseover="makeItVisible('TotSpin')" onmouseout="hideHelp('TotSpin')" href="#">TOTAL 10S</a>
									</div>
								</td>
								<td class="c1headc1" width="11%">
									<div align="center">
										<a class="isTip" onmouseover="makeItVisible('TotNonSpin')" onmouseout="hideHelp('TotNonSpin')" href="#">TOTAL 10N</a>
									</div>
								</td>
								<td class="c1headc1" width="11%">
									<div align="center">
										<a class="isTip" onmouseover="makeItVisible('TotResv')" onmouseout="hideHelp('TotResv')" href="#">TOTAL 30R</a>
									</div>
								</td>
								<td class="c1headc1" width="11%">
									<div align="center">
										<a class="isTip" onmouseover="makeItVisible('OntDemand')" onmouseout="hideHelp('OntDemand')" href="#">ONTARIO DEMAND</a>
									</div>
								</td>
							</tr>
							<xsl:for-each select="ieso:IntervalEnergy">
								<xsl:variable name="changeColor">
									<xsl:choose>
										<xsl:when test="ieso:Interval mod 2 = '1'">
											<xsl:value-of select="1"/>
										</xsl:when>
										<xsl:when test="ieso:Interval mod 2 = '0'">
											<xsl:value-of select="2"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="1"/>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:variable>
								<tr>
									<xsl:choose>
										<xsl:when test="$changeColor= '1'">
											<td class="c2headc1" align="center">
												<xsl:value-of select="ieso:Interval"/>
											</td>
										</xsl:when>
										<xsl:when test="$changeColor= '2'">
											<td class="c2headc2" align="center">
												<xsl:value-of select="ieso:Interval"/>
											</td>
										</xsl:when>
									</xsl:choose>
									<xsl:variable name="flag">
										<xsl:value-of select="translate(ieso:Flag,$smallcase, $uppercase)"/>
									</xsl:variable>
									<xsl:for-each select="ieso:MQ">
										<xsl:choose>
											<!--show the energy in black if flag is 'DSO-RD' for intertie zones otherwise red-->
											<xsl:when test="contains($flag, 'DSO-RD')">
												<xsl:choose>
													<xsl:when test="$changeColor= '1'">
														<td class="c1datac1" align="center">
															<xsl:value-of select="format-number(ieso:EnergyMW,'#0.0')"/>
														</td>
													</xsl:when>
													<xsl:when test="$changeColor= '2'">
														<td class="c1datac2" align="center">
															<xsl:value-of select="format-number(ieso:EnergyMW,'#0.0')"/>
														</td>
													</xsl:when>
												</xsl:choose>
											</xsl:when>
											<xsl:otherwise>
												<xsl:choose>
													<xsl:when test="$changeColor= '1'">
														<td class="c1datac1" align="center">
															<font color="red">
																<xsl:value-of select="format-number(ieso:EnergyMW,'#0.0')"/>
															</font>
														</td>
													</xsl:when>
													<xsl:when test="$changeColor= '2'">
														<td class="c1datac2" align="center">
															<font color="red">
																<xsl:value-of select="format-number(ieso:EnergyMW,'#0.0')"/>
															</font>
														</td>
													</xsl:when>
												</xsl:choose>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:for-each>
								</tr>
							</xsl:for-each>
						</table>
					</xsl:for-each>
				</div>
			</body>
		</html>
	</xsl:template>
	<xsl:template name="Month">
		<xsl:param name="month"/>
		<xsl:choose>
			<xsl:when test="$month='01'">
				<xsl:text>Jan</xsl:text>
			</xsl:when>
			<xsl:when test="$month='02'">
				<xsl:text>Feb</xsl:text>
			</xsl:when>
			<xsl:when test="$month='03'">
				<xsl:text>Mar</xsl:text>
			</xsl:when>
			<xsl:when test="$month='04'">
				<xsl:text>Apr</xsl:text>
			</xsl:when>
			<xsl:when test="$month='05'">
				<xsl:text>May</xsl:text>
			</xsl:when>
			<xsl:when test="$month='06'">
				<xsl:text>Jun</xsl:text>
			</xsl:when>
			<xsl:when test="$month='07'">
				<xsl:text>Jul</xsl:text>
			</xsl:when>
			<xsl:when test="$month='08'">
				<xsl:text>Aug</xsl:text>
			</xsl:when>
			<xsl:when test="$month='09'">
				<xsl:text>Sep</xsl:text>
			</xsl:when>
			<xsl:when test="$month='10'">
				<xsl:text>Oct</xsl:text>
			</xsl:when>
			<xsl:when test="$month='11'">
				<xsl:text>Nov</xsl:text>
			</xsl:when>
			<xsl:when test="$month='12'">
				<xsl:text>Dec</xsl:text>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="Time">
		<xsl:param name="hour"/>
		<xsl:choose>
			<xsl:when test="$hour &lt; '13'">
				<xsl:text>a.m.</xsl:text>
			</xsl:when>
			<xsl:when test="$hour &gt; '12'">
				<xsl:text>p.m.</xsl:text>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>

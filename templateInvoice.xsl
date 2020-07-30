<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<!-- Attribute used for table border -->
	<xsl:attribute-set name="tableBorder">
		<xsl:attribute name="border">solid 0.1mm black</xsl:attribute>
	</xsl:attribute-set>
	<xsl:template match="InvoiceData">
		<fo:root>
			<fo:layout-master-set>
				<fo:simple-page-master master-name="simpleA4"
					page-height="29.7cm" page-width="25.0cm" margin="1cm">
					<fo:region-body />
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="simpleA4">
				<fo:flow flow-name="xsl-region-body">
					<fo:block font-size="26pt" font-family="Arial"
						color="black" font-weight="bold" space-after="5mm">
						Order Invoice-
						<xsl:value-of select="ID" />
					</fo:block>
					<fo:block font-size="10pt">
						<fo:table table-layout="fixed" width="100%"
							border-collapse="separate">
							<fo:table-column column-width="2cm" />
							<fo:table-column column-width="5cm" />
							<fo:table-column column-width="5cm" />
							<fo:table-column column-width="3cm" />
							<fo:table-column column-width="3cm" />
							<fo:table-column column-width="3cm" />
							<fo:table-column column-width="3cm" />
							<fo:table-header font-weight="bold">
								<fo:table-cell border="1pt solid black"
									xsl:use-attribute-sets="tableBorder">
									<fo:block font-size="15pt" text-align="center"
										font-weight="bold">S.NO.</fo:block>
								</fo:table-cell>
								<fo:table-cell border="1pt solid black"
									xsl:use-attribute-sets="tableBorder">
									<fo:block font-size="15pt" text-align="center"
										font-weight="bold">ProductName</fo:block>
								</fo:table-cell>
								<fo:table-cell border="1pt solid black"
									xsl:use-attribute-sets="tableBorder">
									<fo:block font-size="15pt" text-align="center"
										font-weight="bold">Barcode</fo:block>
								</fo:table-cell>
								<fo:table-cell border="1pt solid black"
									xsl:use-attribute-sets="tableBorder">
									<fo:block font-size="15pt" text-align="center"
										font-weight="bold">Quantity</fo:block>
								</fo:table-cell>
								<fo:table-cell border="1pt solid black"
									xsl:use-attribute-sets="tableBorder">
									<fo:block font-size="15pt" text-align="center"
										font-weight="bold">MRP</fo:block>
								</fo:table-cell>
								<fo:table-cell border="1pt solid black"
									xsl:use-attribute-sets="tableBorder">
									<fo:block font-size="15pt" text-align="center"
										font-weight="bold">Selling Price</fo:block>
								</fo:table-cell>
								<fo:table-cell border="1pt solid black"
									xsl:use-attribute-sets="tableBorder">
									<fo:block font-size="15pt" text-align="center"
										font-weight="bold">Total Amount</fo:block>
								</fo:table-cell>
							</fo:table-header>
							<fo:table-body>
								<xsl:apply-templates select="invoice" /> <!-- branch tag is taken and pasted in below used template 46 line -->
							</fo:table-body>
						</fo:table>
					</fo:block>
					<fo:block font-size="25pt" text-align="right"
						font-family="Helvetica" color="black" font-weight="bold"
						space-after="5mm">
						Total Amount : Rs.
						<xsl:value-of select="totalAmount" />
						/-
					</fo:block>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	<xsl:template match="invoice">
		<fo:table-row>
			<fo:table-cell border="1pt solid black"
				xsl:use-attribute-sets="tableBorder">
				<fo:block text-align="center" font-size="15pt">
					<xsl:value-of select="sno" />
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border="1pt solid black"
				xsl:use-attribute-sets="tableBorder">
				<fo:block text-align="center" font-size="15pt">
					<xsl:value-of select="name" />
				</fo:block>
			</fo:table-cell>

			<fo:table-cell border="1pt solid black"
				xsl:use-attribute-sets="tableBorder">
				<fo:block text-align="center" font-size="15pt">
					<xsl:value-of select="barcode" />
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border="1pt solid black"
				xsl:use-attribute-sets="tableBorder">
				<fo:block text-align="center" font-size="15pt">
					<xsl:value-of select="qty" />
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border="1pt solid black"
				xsl:use-attribute-sets="tableBorder">
				<fo:block text-align="center" font-size="15pt">
					<xsl:value-of select="mrp" />
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border="1pt solid black"
				xsl:use-attribute-sets="tableBorder">
				<fo:block text-align="center" font-size="15pt">
					<xsl:value-of select="sp" />
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border="1pt solid black"
				xsl:use-attribute-sets="tableBorder">
				<fo:block text-align="center" font-size="15pt">
					<xsl:value-of select="totalPrice" />
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>
</xsl:stylesheet>
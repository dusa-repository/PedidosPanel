USE [dusa_pedidos]
GO
/****** Object:  User [wapclient]    Script Date: 04/09/2007 14:33:59 ******/
CREATE USER [wapclient] FOR LOGIN [wapclient] WITH DEFAULT_SCHEMA=[dbo]
GO
/****** Object:  Table [dbo].[customers]    Script Date: 04/09/2007 14:33:59 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[customers](
	[customer_id] [varchar](8) NOT NULL,
	[name] [varchar](35) NULL,
	[credit_limit] [float] NULL,
	[region] [varchar](3) NULL,
	[salesman_id] [varchar](3) NULL,
	[price_index] [tinyint] NULL,
	[warehouse] [varchar](12) NULL,
 CONSTRAINT [PK_customers] PRIMARY KEY CLUSTERED 
(
	[customer_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[products]    Script Date: 04/09/2007 14:33:59 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[products](
	[product_id] [varchar](10) NOT NULL,
	[description] [varchar](35) NULL,
	[stock] [float] NULL,
	[price1] [float] NULL,
	[price2] [float] NULL,
	[price3] [float] NULL,
	[price4] [float] NULL,
	[price5] [float] NULL,
	[price6] [float] NULL,
	[price7] [float] NULL,
	[price8] [float] NULL,
	[price9] [float] NULL,
	[price10] [float] NULL,
	[primary_unit] [varchar](2) NULL,
	[alternative_unit] [varchar](2) NULL,
	[conversion_rate] [float] NULL,
	[warehouse] [varchar](12) NULL,
 CONSTRAINT [PK_products] PRIMARY KEY CLUSTERED 
(
	[product_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[salesmen]    Script Date: 04/09/2007 14:33:59 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[salesmen](
	[salesman_id] [varchar](3) NOT NULL,
	[name] [varchar](35) NULL,
	[region] [varchar](3) NULL,
	[top_serial] [int] NULL,
	[bottom_serial] [int] NULL,
	[comments] [varchar](10) NULL,
 CONSTRAINT [PK_salesmen] PRIMARY KEY CLUSTERED 
(
	[salesman_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[orders]    Script Date: 04/09/2007 14:33:59 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[orders](
	[order_id] [int] NOT NULL,
	[order_date] [varchar](8) NOT NULL,
	[order_time] [varchar](11) NOT NULL,
	[salesman_id] [varchar](3) NOT NULL,
	[customer_id] [varchar](8) NOT NULL,
	[delivery_date] [varchar](8) NULL,
	[status] [varchar](3) NULL,
	[amount] [float] NULL,
 CONSTRAINT [PK_orders] PRIMARY KEY CLUSTERED 
(
	[order_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[orders_details]    Script Date: 04/09/2007 14:33:59 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[orders_details](
	[order_id] [int] NOT NULL,
	[product_id] [varchar](50) NOT NULL,
	[unit] [varchar](2) NOT NULL,
	[qty] [float] NOT NULL,
	[subtotal] [float] NOT NULL,
 CONSTRAINT [PK_orders_details] PRIMARY KEY CLUSTERED 
(
	[order_id] ASC,
	[product_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO

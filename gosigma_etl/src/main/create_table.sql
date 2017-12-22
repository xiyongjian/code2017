USE <DBNAME>

-- DROP TABLE IESO_REALTIMECONSTTOTALS;
CREATE TABLE IF NOT EXISTS IESO_RealtimeConstTotals (
    DDATE DATE,
    HOUR INT,
    DINTERVAL INT,
    TOTAL_ENERGY FLOAT,
    TOTAL_10S FLOAT,
    TOTAL_10N FLOAT,
    TOTAL_30R FLOAT,
    TOTAL_DISP_LOAD FLOAT,
    TOTAL_LOAD FLOAT,
    TOTAL_LOSS FLOAT,
    PRIMARY KEY (DDATE, HOUR, DINTERVAL)
    );


-- DROP TABLE NYISO_5_Minute_Price;
CREATE TABLE IF NOT EXISTS NYISO_5_Minute_Price (
    DDATE DATE,
    HOUR INT,
    DINTERVAL INT,
    NAME CHAR(10),
    PTID INT,
    LBMP FLOAT,
    MARGINAL_COST_LOSSES FLOAT,
    MARGINAL_COST_CONGESTION FLOAT,
    PRIMARY KEY (DDATE, HOUR, DINTERVAL, PTID)
    );

DROP TABLE PJM_5_minute_Prices;
CREATE TABLE IF NOT EXISTS PJM_5_minute_Prices (
    DDATE DATE,
    HOUR INT,
    DINTERVAL INT,
    NAME VARCHAR(64),
    DTYPE VARCHAR(64),
    PRICE FLOAT,
    PRIMARY KEY (DDATE, HOUR, DINTERVAL, NAME, DTYPE)
);

DROP TABLE PJM_Hourly_Price;
CREATE TABLE IF NOT EXISTS PJM_Hourly_Price (
    DDATE DATE,
    HOUR INT,
    NAME VARCHAR(64),
    DTYPE VARCHAR(64),
    PRICE FLOAT,
    PRIMARY KEY (DDATE, HOUR, NAME, DTYPE)
);

DROP TABLE PJM_5_Minute_Actual_Load;
CREATE TABLE IF NOT EXISTS PJM_5_Minute_Actual_Load (
    DDATE DATE,
    HOUR INT,
    DINTERVAL INT,
    AREA VARCHAR(64),
    DLOAD FLOAT,
    PRIMARY KEY (DDATE, HOUR, DINTERVAL, AREA)
);

DROP TABLE PJM_DA_Prices;
CREATE TABLE PJM_DA_Prices (
    DDate DATE,
    Hour_End INT,
    PnodeID INT,
    Name VARCHAR(64),
    DType VARCHAR(64),
    TotalLMP_100 FLOAT,
    CongestionPrice_100 FLOAT,
    MarginalLossPrice_100 FLOAT,
    TotalLMP_200 FLOAT,
    CongestionPrice_200 FLOAT,
    MarginalLossPrice_200 FLOAT,
    TotalLMP_300 FLOAT,
    CongestionPrice_300 FLOAT,
    MarginalLossPrice_300 FLOAT,
    TotalLMP_400 FLOAT,
    CongestionPrice_400 FLOAT,
    MarginalLossPrice_400 FLOAT,
    TotalLMP_500 FLOAT,
    CongestionPrice_500 FLOAT,
    MarginalLossPrice_500 FLOAT,
    TotalLMP_600 FLOAT,
    CongestionPrice_600 FLOAT,
    MarginalLossPrice_600 FLOAT,
    TotalLMP_700 FLOAT,
    CongestionPrice_700 FLOAT,
    MarginalLossPrice_700 FLOAT,
    TotalLMP_800 FLOAT,
    CongestionPrice_800 FLOAT,
    MarginalLossPrice_800 FLOAT,
    TotalLMP_900 FLOAT,
    CongestionPrice_900 FLOAT,
    MarginalLossPrice_900 FLOAT,
    TotalLMP_1000 FLOAT,
    CongestionPrice_1000 FLOAT,
    MarginalLossPrice_1000 FLOAT,
    TotalLMP_1100 FLOAT,
    CongestionPrice_1100 FLOAT,
    MarginalLossPrice_1100 FLOAT,
    TotalLMP_1200 FLOAT,
    CongestionPrice_1200 FLOAT,
    MarginalLossPrice_1200 FLOAT,
    TotalLMP_1300 FLOAT,
    CongestionPrice_1300 FLOAT,
    MarginalLossPrice_1300 FLOAT,
    TotalLMP_1400 FLOAT,
    CongestionPrice_1400 FLOAT,
    MarginalLossPrice_1400 FLOAT,
    TotalLMP_1500 FLOAT,
    CongestionPrice_1500 FLOAT,
    MarginalLossPrice_1500 FLOAT,
    TotalLMP_1600 FLOAT,
    CongestionPrice_1600 FLOAT,
    MarginalLossPrice_1600 FLOAT,
    TotalLMP_1700 FLOAT,
    CongestionPrice_1700 FLOAT,
    MarginalLossPrice_1700 FLOAT,
    TotalLMP_1800 FLOAT,
    CongestionPrice_1800 FLOAT,
    MarginalLossPrice_1800 FLOAT,
    TotalLMP_1900 FLOAT,
    CongestionPrice_1900 FLOAT,
    MarginalLossPrice_1900 FLOAT,
    TotalLMP_2000 FLOAT,
    CongestionPrice_2000 FLOAT,
    MarginalLossPrice_2000 FLOAT,
    TotalLMP_2100 FLOAT,
    CongestionPrice_2100 FLOAT,
    MarginalLossPrice_2100 FLOAT,
    TotalLMP_2200 FLOAT,
    CongestionPrice_2200 FLOAT,
    MarginalLossPrice_2200 FLOAT,
    TotalLMP_2300 FLOAT,
    CongestionPrice_2300 FLOAT,
    MarginalLossPrice_2300 FLOAT,
    TotalLMP_2400 FLOAT,
    CongestionPrice_2400 FLOAT,
    MarginalLossPrice_2400 FLOAT,
    PRIMARY KEY (DDATE, PnodeID, Name)
);

-------------------------- e05 ----------------------------
DROP TABLE PJM_Hourly_Load ;
CREATE TABLE PJM_Hourly_Load (
	DDate DATE NOT NULL,
	HourEnd INT NOT NULL,
	MIDATL FLOAT,
	AP FLOAT,
	NI FLOAT,
	AEP FLOAT,
	DAY FLOAT,
	DUQ FLOAT,
	DOM FLOAT,
	ATSI FLOAT,
	DEOK FLOAT,
	EKPC FLOAT,
	PRIMARY KEY (DDate, HourEnd)
);

-------------------------- e06 MISO_Consolidated ----------------------------
DROP TABLE MISO_5_minute_prices ;
CREATE TABLE MISO_5_minute_prices (
  DDATE DATE,
  HOUR INT,
  DINTERVAL INT,
  NAME VARCHAR(64),
  LMP FLOAT,
  MLC FLOAT,
  MCC FLOAT,
  PRIMARY KEY (DDATE, HOUR, DINTERVAL, NAME)
  );

DROP TABLE MISO_hourly_prices ;
CREATE TABLE MISO_hourly_prices (
  DDATE DATE,
  HOUR INT,
  NAME VARCHAR(64),
  LMP FLOAT,
  MLC FLOAT,
  MCC FLOAT,
  PRIMARY KEY (DDATE, HOUR, NAME)
  );

-------------------------- e07 MISO_TotalLoad ----------------------------
DROP TABLE MISO_DA_Cleared_Load ;
CREATE TABLE MISO_DA_Cleared_Load (
  DDate DATE,
  Hour INT,
  DLoad FLOAT,
  PRIMARY KEY (DDate, Hour)
  );

DROP TABLE MISO_Load_Forecast ;
CREATE TABLE MISO_Load_Forecast (
  DDate DATE,
  Hour INT,
  Load_Forecast FLOAT,
  PRIMARY KEY (DDate, Hour)
  );

DROP TABLE MISO5_minute_Load ;
CREATE TABLE MISO5_minute_Load (
  DDate DATE,
  Hour INT,
  DInterval INT,
  DLoad FLOAT,
  PRIMARY KEY (DDate, Hour, DInterval)
  );


-------------------------- e08 MISO_DA_Prices ----------------------------
DROP TABLE MISO_DA_Prices ;
CREATE TABLE MISO_DA_Prices (
	DDate DATE,
	Node VARCHAR(64),
	DType VARCHAR(64),
	Value VARCHAR(64),
	HE_1 FLOAT,
	HE_2 FLOAT,
	HE_3 FLOAT,
	HE_4 FLOAT,
	HE_5 FLOAT,
	HE_6 FLOAT,
	HE_7 FLOAT,
	HE_8 FLOAT,
	HE_9 FLOAT,
	HE_10 FLOAT,
	HE_11 FLOAT,
	HE_12 FLOAT,
	HE_13 FLOAT,
	HE_14 FLOAT,
	HE_15 FLOAT,
	HE_16 FLOAT,
	HE_17 FLOAT,
	HE_18 FLOAT,
	HE_19 FLOAT,
	HE_20 FLOAT,
	HE_21 FLOAT,
	HE_22 FLOAT,
	HE_23 FLOAT,
	HE_24 FLOAT,
	PRIMARY KEY (DDate, Node, DType, Value)
	);
-------------------------- e09 MISO_Final_Real_Time_Hourly_Prices ----------------------------
DROP TABLE MISO_Final_Real_Time_Hourly_Prices ;
CREATE TABLE MISO_Final_Real_Time_Hourly_Prices (
	DDate DATE,
	Node VARCHAR(64),
	DType VARCHAR(64),
	Value VARCHAR(64),
	HE_1 FLOAT,
	HE_2 FLOAT,
	HE_3 FLOAT,
	HE_4 FLOAT,
	HE_5 FLOAT,
	HE_6 FLOAT,
	HE_7 FLOAT,
	HE_8 FLOAT,
	HE_9 FLOAT,
	HE_10 FLOAT,
	HE_11 FLOAT,
	HE_12 FLOAT,
	HE_13 FLOAT,
	HE_14 FLOAT,
	HE_15 FLOAT,
	HE_16 FLOAT,
	HE_17 FLOAT,
	HE_18 FLOAT,
	HE_19 FLOAT,
	HE_20 FLOAT,
	HE_21 FLOAT,
	HE_22 FLOAT,
	HE_23 FLOAT,
	HE_24 FLOAT,
	PRIMARY KEY (DDate, Node, DType, Value)
	);

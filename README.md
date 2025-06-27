# House Price Prediction using Hadoop and Scikit-Learn

## Abstract

This project demonstrates a comprehensive big data analytics pipeline for predicting house prices in India. The process begins with data cleaning and preprocessing using Java-based MapReduce programs on the Apache Hadoop framework. Following the initial cleaning, the project leverages Python's scientific computing stack (Pandas, Matplotlib, Scikit-learn) for in-depth exploratory data analysis (EDA), visualization, and the development of a predictive Multiple Linear Regression model. The analysis reveals key features influencing house prices and evaluates the performance of the predictive model.

---

## Project Overview

The primary goal of this project is to analyze the factors affecting house prices in India and to build a model capable of predicting these prices.

The methodology is broken down into two main stages:

1.  **Data Cleaning with Hadoop MapReduce:**
    * The raw dataset is processed in a distributed environment using a series of Java MapReduce jobs.
    * Tasks include: identifying and removing duplicate records, checking for empty features, removing constant or irrelevant columns (`BHK_OR_RK`), and converting units (Square Feet to Square Meters, INR Lacs to GBP).

2.  **Analysis and Modeling with Python:**
    * The cleaned data is loaded into a Jupyter Notebook for analysis.
    * **Exploratory Data Analysis (EDA):** Visualizations like histograms, pie charts, and scatter plots are used to understand data distributions and relationships. A correlation matrix is generated to quantify the relationships between numerical features.
    * **Predictive Modeling:** A Multiple Linear Regression model is implemented using Scikit-learn to predict the `TARGET(PRICE_IN_BGP)`. The model is trained and evaluated, and the impact of different features, including the seller type (`POSTED_BY`), is assessed.

---

## Data Origin

The dataset used is from the Kaggle competition **"Predict the house prices in India"** uploaded by user **Anmol Kumar**.

- **Link:** `https://www.kaggle.com/anmolkumar/house-price-prediction-challenge/`

---

## Key Findings

* **Feature Correlation:** The house's area (`SQUARE_M`) has the highest positive correlation (0.41) with its price. The `RESALE` status has the strongest negative correlation (-0.20).
* **Model Performance:** The linear regression models achieved a low Coefficient of Determination (R²) ranging from 0.19 to 0.36. This indicates that a linear model alone is not sufficient to accurately capture the complex factors influencing house prices in this dataset. The model that included all features, including `POSTED_BY` as a numerical category, performed the best among the variations tested.

---

## Code Structure

### Java MapReduce (Data Cleaning)

Located in `src.java`, this code is intended for a Hadoop environment. It contains several MapReduce jobs:

* `DublicateChecker` / `DublicateCounter` / `DublicateRemover`: Finds, counts, and removes duplicate entries. (*Correction: Renamed from "Dublicate"*).
* `FeatureRemover`: Removes the `BHK_OR_RK` column and converts units.
* `EmptyFeatues`: Scans for rows with missing values.

### Python Script (Analysis & Prediction)

The `house_price_analysis.py` script contains the complete workflow for analysis and modeling.

---

## How to Run the Python Analysis

1.  **Prerequisites:**
    * Python 3.x
    * Pandas
    * Matplotlib
    * Scikit-learn
    * Statsmodels

    You can install these packages using pip:
    ```bash
    pip install pandas matplotlib scikit-learn statsmodels
    ```

2.  **Execute the Script:**
    * Ensure the `train.csv` file is in the same directory as the script.
    * Run the script from your terminal:
    ```bash
    python house_price_analysis.py
    ```
    The script will perform the analysis and print the model evaluation results to the console. It will also generate and display plots for the exploratory data analysis.


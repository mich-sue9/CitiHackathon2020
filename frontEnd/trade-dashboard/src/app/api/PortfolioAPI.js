

const url = "http://localhost:8080/api/portfolios/";
export const portId = "5f6296c2200dd06478c0ede0";

export const getPortfolio = function(portfolioId){
    return fetch(url+portfolioId);
}

export const getStockList = function(portfolioId){
    return fetch(url+"getStockLivePrice/"+portfolioId);
}
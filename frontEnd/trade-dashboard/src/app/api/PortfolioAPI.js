

const url = "http://localhost:8080/api/portfolios/";
export const portId = "5f637f50f8e8ed1ebd4ac8cf";

export const getPortfolio = function(portfolioId){
    return fetch(url+portfolioId);
}

export const getStockList = function(portfolioId){
    return fetch(url+"getStockLivePrice/"+portfolioId);
}



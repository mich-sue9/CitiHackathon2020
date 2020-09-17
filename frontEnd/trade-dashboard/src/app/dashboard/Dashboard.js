import React, { Component } from 'react';
import {Doughnut} from 'react-chartjs-2';
import {getPortfolio,getStockList, portId} from '../api/PortfolioAPI';
import * as scale from "d3-scale";


const formatter = new Intl.NumberFormat('en-US', {
  style: 'currency',
  currency: 'USD',
});

export class Dashboard extends Component {
  handleChange = date => {
    this.setState({
      startDate: date,

    });
  };
  constructor(props){
    super(props)
    //this.fetchPrices = this.fetchPrices.bind(this);
    this.state = {
      error: false,

      intervalId:"",
      portfolioId: portId,
      liveData:[],
      valuation : "",
      portfolioData:"",
      loadPortfolio: false,
    } 
  }

  //get the portfolioData upon load
  loadPortfolio(){
    //get the reponse from the backend
    let response = getPortfolio(this.state.portfolioId);

    //parse response
    response
      .then(resp => resp.json())
      .then(
        data => {
          //console.log(data)
          data.totalExpense = formatter.format(data.totalExpense);
          this.setState({
            loadPortfolio: true,
            portfolioData: data,
            
          });
          
        },
        err =>{
          this.setState({
            loadPortfolio:false,
          });
        }
      );  
  }

  loadLiveStockPrice(){
    //get the livestock price 
   let response = getStockList(this.state.portfolioId);
    response
      .then(res => res.json())
      .then( data => {
      //  console.log(data)
        this.setState(
          {valuation: formatter.format(data.valuation),
           liveData: data.stockPrice})
      });
  }


 componentDidMount(){
    // Retrieve stock profile valuation & stock prices 
    console.log("Component mounted");
    this.loadLiveStockPrice();
    this.loadPortfolio();
    this.fetchPricesRefresher = setInterval(() => {
        this.loadLiveStockPrice();
        this.loadPortfolio();
    }, 5000)
    
  }


  render () {
    let l = this.state.liveData.length
  
    //create a color list
    const colors = scale.scaleLinear().domain([0, l]).range(["#1D9A6C", "#3B73B4"]);
    console.log(colors);

    let colorList =[]
    for(var i=0;i<l;i++){
      colorList.push(colors(i))
    }

  
    const stockData = {
      datasets: [{
        data: this.state.liveData.map(s => s.quantity),
        backgroundColor: colorList
      }],
      // These labels appear in the legend and in the tooltips when hovering different arcs
      labels: this.state.liveData.map(s => s.ticker)
    };
    return (
      <div>
        <div className="page-header">
          <h3 className="page-title">
            <span className="page-title-icon bg-gradient-primary text-white mr-2">
              <i className="mdi mdi-home"></i>
            </span> Dashboard </h3>
          <nav aria-label="breadcrumb">
            <ul className="breadcrumb">
              <li className="breadcrumb-item active" aria-current="page">
                <span></span>Overview <i className="mdi mdi-alert-circle-outline icon-sm text-primary align-middle"></i>
              </li>
            </ul>
          </nav>
        </div>
        <div className="row">
          <div className="col-md-6 stretch-card grid-margin">
            <div className="card bg-gradient-danger card-img-holder text-white">
              <div className="card-body">
                <img src={require("../../assets/images/dashboard/circle.svg")} className="card-img-absolute" alt="circle" />
                <h4 className="font-weight-normal mb-3">Portfolio Valuation <i className="mdi mdi-chart-line mdi-24px float-right"></i>
                </h4>
                <h2 className="mb-5"> {this.state.valuation} </h2>
                <h6 className="card-text"> </h6>
              </div>
            </div>
          </div>
          <div className="col-md-6 stretch-card grid-margin">
            <div className="card bg-gradient-info card-img-holder text-white">
              <div className="card-body">
                <img src={require("../../assets/images/dashboard/circle.svg")} className="card-img-absolute" alt="circle" />
                <h4 className="font-weight-normal mb-3">Total Expense <i className="mdi mdi-bookmark-outline mdi-24px float-right"></i>
                </h4>
                <h2 className="mb-5">{this.state.portfolioData.totalExpense}</h2>
              </div>
            </div>
          </div>
        </div>
        <div className="row">
          <div className="col-md-7 grid-margin stretch-card">
            <div className="card">
              <div className="card-body">
                <h4 className="card-title">Live Stock Prices*</h4>
                <div className="table-responsive">
                  <table className="table">
                    <thead>
                      <tr>
                        <th> Ticker </th>
                        <th> Quantity owned in Portfolio </th>
                        <th> Last Close price </th>
                      </tr>
                    </thead>
                    <tbody>
                        {this.state.liveData.map((stock)=> {
                          return(
                            <tr>
                              <td> {stock.ticker} </td>
                              <td> {stock.quantity} </td>
                              <td> {formatter.format(stock.price)} </td>
                            </tr> );
                        })}                       
                    </tbody>
                  </table>                 
                </div>
              </div>
            </div>
          </div>
          <div className="col-md-5 grid-margin stretch-card">
            <div className="card">
              <div className="card-body">
                <h4 className="card-title">Stocks In Portfolio</h4>
                <Doughnut data={stockData} />
              </div>
            </div>
          </div>
        </div>
      </div> 
    );
  }
}
export default Dashboard;
import React, { Component } from 'react'
import {portId} from '../api/PortfolioAPI';




const formatter = new Intl.NumberFormat('en-US', {
  style: 'currency',
  currency: 'USD',
});



export class BasicTable extends Component {

  constructor(props) {
    super(props);
    this.state = {
      portfolioId: portId,
      trades:[]
    };
  }


  componentDidMount() {
    this.loadTradeHistory();
    this.TradeHistoryRefresher = setInterval(() => {
      this.loadTradeHistory();
    }, 5000)
    
  }

  loadTradeHistory = () => {
    let response = fetch('http://localhost:8080/api/portfolios/history/' + this.state.portfolioId);

    response
      .then(res => res.json())
      .then(
        result => {
          this.setState({
            isLoaded: true,
            trades: result,           
          });
        },
        error => {
          this.setState({
            isLoaded: false,
          });
        }
      );
  };

  renderTrades(){

  }

  
   


  render(trades) {


      return (
      <div>
        <div className="page-header">
          <h3 className="page-title"> All trade history </h3>
          
          
        </div>
        <div className="row">
          <div className="col-lg-12 grid-margin stretch-card">
            <div className="card">
              <div className="card-body">
                <h4 className="card-title">Trade History</h4>           
                <div className="table-responsive">
                  <table className="table table-bordered">
                    <thead>
                      
                      <tr>
                        <th> Date </th>
                        <th> Ticker </th>
                        <th> Quantity </th>
                        <th> Price </th>
                        <th> Status </th>
                        <th> Total Investment </th>
                      </tr>
                    </thead>
                    <tbody>
                    {this.state.trades.map((trade)=>  
                       <tr>
                        <td>{trade.dateCreated.substring(0,10)} {trade.dateCreated.substring(11,19)}</td>
                        <td>{trade.stockTicker} </td>
                        <td>{trade.quantity}</td>
 
                        <td>{formatter.format(trade.requestPrice)}</td>
                        
                        <td><label className="badge badge-info">{trade.tStatus}</label></td>
                        <td>{formatter.format(trade.requestPrice*trade.quantity)}</td>
                      </tr>

                    )
                   }
                     
                      
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>

        
        </div>
        </div>
      )
    
   

  }





  
}

export default BasicTable

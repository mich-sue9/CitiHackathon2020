import React, { Component } from 'react'
import { ProgressBar } from 'react-bootstrap';

export class BasicTable extends Component {

  constructor(props) {
    super(props);
    this.state = {
      trades:[]
    };
  }


  componentDidMount() {
    this.loadTradeHistory();
    
  }

  loadTradeHistory = () => {
    let response = fetch('http://localhost:8080/api/trades');

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
                        <td>{trade.dateCreated} </td>
                        <td>{trade.stockTicker} </td>
                        <td>{trade.quantity}</td>
 
                        <td>${trade.requestPrice}</td>
                        
                        <td><label className="badge badge-info">{trade.tStatus}</label></td>
                        <td>${trade.requestPrice*trade.quantity}</td>
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

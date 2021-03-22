import React, { Component } from 'react';
import { logout } from './services/auth'
import './Dashboard.css'
import { Card } from 'antd';

const { Meta } = Card;

class Dashboard extends Component {

    handleClickAdd = () => {
        this.props.history.push("/research");
    }

    handleClickVisu = () => {
        this.props.history.push("/visualize");
    }

    handleLogout = () => {
        logout()
        this.props.history.push("/")
    }

    render() {
        return (
            <div>
                <div className="logout">
                    <button onClick={this.handleLogout}>Logout</button>
                </div>
                <div className="Dashboard">
                    <h1 className="Title">Dashboard</h1>
                    <div className="Cards">
                        <Card
                            hoverable
                            cover={<img alt="survey" src={require("./assets/survey.png")} />}
                            onClick={this.handleClickAdd}
                        >
                            <Meta title="Adicionar uma entrevista" />
                        </Card>
                        <Card
                            hoverable
                            cover={<img alt="data-table" src={require("./assets/data-table.png")} />}
                            onClick={this.handleClickVisu}
                        >
                            <Meta title="Visualizar respostas" />
                        </Card>

                    </div>
                    {/* <button onClick={this.handleClickAdd}>Adicionar uma entrevista </button> */}
                    {/* <button onClick={this.handleClickVisu}>Visualizar respostas</button> */}
                </div>
            </div>
        )
    }
}
export default Dashboard;
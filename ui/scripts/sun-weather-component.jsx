import React from 'react';
import axios from 'axios';

class SunWeatherComponent extends React.Component
{
    constructor(props) {
        super(props);
        this.state = {
            sunrise: undefined,
            sunset: undefined,
            temperature: undefined,
            requests: undefined
        };
    }

    componentDidMount = () => {
        axios.get('/data').then(
            (response) => {
                console.log("data received by axios: ", response)
                const json = response.data;
                this.setState({
                    sunrise: json.sunInfo.sunrise,
                    sunset:  json.sunInfo.sunset,
                    temperature: json.temperature,
                    requests: json.requests
                });
            }
        )
    };

    render = () => {
        return <table><tbody>
                <tr>
                    <td>Sunrise time</td>
                    <td>{this.state.sunrise}</td>
                </tr>
                <tr>
                    <td>Sunset time</td>
                    <td>{this.state.sunset}</td>
                </tr>
                <tr>
                    <td>Current temperature</td>
                    <td>{this.state.temperature}</td>
                </tr>
                <tr>
                    <td>Requests</td>
                    <td>{this.state.requests}</td>
                </tr>
            </tbody></table>
    }


}

export default SunWeatherComponent;

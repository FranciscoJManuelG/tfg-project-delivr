import React, {useState} from 'react';
import {useDispatch} from 'react-redux';
import {FormattedMessage} from 'react-intl';
import {useParams, useHistory} from 'react-router-dom';

import {Errors} from '../../common';
import * as actions from '../actions';

const SetDateAndDiners = () => {

    const dispatch = useDispatch();
    const history = useHistory();
    var dateNow = new Date();
    const [reservationDate, setReservationDate] = useState(dateNow.toISOString().split('T')[0]);
    const [periodType, setPeriodType] = useState('LUNCH');
    const [diners, setDiners] = useState(1);
    const [backendErrors, setBackendErrors] = useState(null);
    const {id} = useParams();
    let form;
    

    const handleSubmit = event => {

        event.preventDefault();

        if (form.checkValidity()) {
            
            dispatch(actions.checkCapacity(
                id,
                reservationDate,
                periodType.trim(),
                diners,
                () => history.push(`/reservation/find-menu-products/${id}/${reservationDate}/${periodType}/${diners}`),
                errors => setBackendErrors(errors)
            ));

            console.log(dispatch(actions.checkCapacity(
                id,
                reservationDate,
                periodType.trim(),
                diners,
                () => history.push(`/reservation/find-menu-products/${id}/${reservationDate}/${periodType}/${diners}`),
                errors => setBackendErrors(errors)
            )));

        } else {

            setBackendErrors(null);
            form.classList.add('was-validated');

        }
    }

    return (
        <div className="container">
            <div className="row justify-content-center" >
                <aside className="col-sm-6">
                    <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>
                    <div className="card">
                        <article className="card-body">
                	        <h4 className="card-title mb-4 mt-1">Establece la fecha en la que desea realizar una reserva y los comensales que asisitirán</h4>
                	        <br/>
                            <form ref={node => form = node} className="needs-validation" 
                                noValidate onSubmit={e => handleSubmit(e)}>
                                <div className="form-group">
                                    <label htmlFor="reservationDate">Fecha: </label>
                                    <input type="date" id="reservationDate" min={dateNow.toISOString().split('T')[0]} className="form-control" 
                                        value={reservationDate}
                                        onChange={e => setReservationDate(e.target.value)}
                                        autoFocus
                                        required/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="diners">Comensales: </label>
                                    <input type="number" id="diners" min="1" step ="1" className="form-control" 
                                        placeholder="Comensales"
                                        value={diners}
                                        onChange={e => setDiners(e.target.value)}
                                        autoFocus
                                        required/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div> 
                                <div>
                                    <label htmlFor="discountType">Hora de reserva: </label>
                                    <div className="form-check-inline">
                                        <input type="radio" id="periodType" name="periodType" className="form-check-input" 
                                            value={'LUNCH'}
                                            onChange={e => setPeriodType(e.target.value)}
                                            autoFocus
                                            checked/>
                                        <label className="form-check-label" htmlFor="discountType">Comida</label>
                                    </div>
                                    <div className="form-check-inline">
                                        <input type="radio" id="periodType" name="periodType2" className="form-check-input" 
                                            value={'DINER'}
                                            onChange={e => setPeriodType(e.target.value)}
                                            autoFocus/>
                                        <label className="form-check-label" htmlFor="periodType2">Cena</label>
                                    </div>
                                </div>
                                <br/>
                                <div className="row">
                                    <div className="col">
                                        <div className="form-group">
                                            <button type="submit" className="btn btn-primary btn-block">Siguiente</button>
                                        </div> 
                                    </div>
                                </div>                                                              
                            </form>
                        </article>
                    </div>
	            </aside> 
            </div>
        </div> 
    );

}

export default SetDateAndDiners;
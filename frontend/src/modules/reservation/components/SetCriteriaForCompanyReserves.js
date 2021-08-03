import React, {useState} from 'react';
import {FormattedMessage} from 'react-intl';
import {useHistory} from 'react-router-dom';

const SetCriteriaForCompanyReserves = () => {

    const history = useHistory();
    var dateNow = new Date();
    const [reservationDate, setReservationDate] = useState(dateNow.toISOString().split('T')[0]);
    const [periodType, setPeriodType] = useState('LUNCH');
    let form;
    

    const handleSubmit = event => {

        event.preventDefault();

        if (form.checkValidity()) {
            
            history.push(`/reservation/find-company-reserves/${reservationDate}/${periodType}`);

        } else {

            form.classList.add('was-validated');

        }
    }

    return (
        <div className="container">
            <div className="row justify-content-center" >
                <aside className="col-sm-6">
                    <div className="card">
                        <article className="card-body">
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

export default SetCriteriaForCompanyReserves;
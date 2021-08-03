import React from 'react';
import {useSelector} from 'react-redux';
import {Link} from 'react-router-dom';
import {FormattedMessage} from 'react-intl';

import users from '../../users';
import business from '../../business';

const Header = () => {

    const userName = useSelector(users.selectors.getUserName);
    const role = useSelector(users.selectors.getRole);
    const existsCompany = useSelector(business.selectors.existsCompany)
    
    return (

        <nav className="navbar navbar-expand-lg navbar-light">
            <Link className="navbar-brand" to="/">Delivr</Link>
            <button className="navbar-toggler" type="button" 
                data-toggle="collapse" data-target="#navbarSupportedContent" 
                aria-controls="navbarSupportedContent" aria-expanded="false" 
                aria-label="Toggle navigation">
                <span className="navbar-toggler-icon"></span>
            </button>

            <div className="collapse navbar-collapse" id="navbarSupportedContent">

                <ul className="navbar-nav mr-auto">
                    {role === "BUSINESSMAN" && existsCompany &&
                        <div>
                            <Link className="dropdown-item" to="/shopping/find-company-orders">
                                Pedidos recibidos
                            </Link>
                        </div>
                    }
                    {role === "BUSINESSMAN" && existsCompany &&
                        <div>
                            <Link className="dropdown-item" to="/reservation/set-criteria-for-company-reserves">
                                Reservas recibidas
                            </Link>
                        </div>
                    }
                    {userName &&
                        <div>
                            <Link className="dropdown-item" to="/shopping/find-user-orders">
                                Pedidos realizados
                            </Link>
                        </div>
                    }
                    {userName &&
                        <div>
                            <Link className="dropdown-item" to="/reservation/find-user-reserves">
                                Reservas realizadas
                            </Link>
                        </div>
                    }
                    {userName &&
                        <div>
                            <Link className="dropdown-item" to="/shopping/find-discount-tickets">
                                Tickets descuento
                            </Link>
                        </div>
                    }
                    {userName &&
                        <div>
                            <Link className="dropdown-item" to="/reservation/find-user-event-evaluations">
                                Valoraciones
                            </Link>
                        </div>
                    }
                    
                </ul>

                {userName ? 

                <ul className="navbar-nav">
                
                    <li className="nav-item dropdown">

                        <a className="dropdown-toggle nav-link" href="/"
                            data-toggle="dropdown">
                            <span className="fas fa-user"></span>&nbsp;
                            {userName}
                        </a>
                        
                        <div className="dropdown-menu dropdown-menu-right">
                            <Link className="dropdown-item" to="/users/change-password">
                                <FormattedMessage id="project.users.UserSettings.title"/>
                            </Link>

                            {role === "BUSINESSMAN" && existsCompany &&
                                <div>
                                    <div>
                                        <Link className="dropdown-item" to="/business/modify-company">
                                            <FormattedMessage id="project.business.BusinessSettings.title"/>
                                        </Link>
                                    </div>
                                    <div>
                                        <Link className="dropdown-item" to="/management/find-products">
                                            <FormattedMessage id="project.business.Products.title"/>
                                        </Link>
                                    </div>
                                </div>
                            }
                            {role === "BUSINESSMAN" && !existsCompany &&     
                                <div>
                                    <Link className="dropdown-item" to="/business/add-company">
                                        <FormattedMessage id="project.business.AddCompany.title"/>
                                    </Link>
                                </div>
                            }

                            <div className="dropdown-divider"></div>
                            <Link className="dropdown-item" to="/users/logout">
                                <FormattedMessage id="project.app.Header.logout"/>
                            </Link>
                        </div>

                    </li>

                </ul>
                
                :

                <ul className="navbar-nav">
                    <li className="nav-item">
                        <Link className="nav-link" to="/users/login">
                            <FormattedMessage id="project.users.Login.title"/>
                        </Link>
                    </li>
                </ul>
                
                }

            </div>
        </nav>

    );

};

export default Header;

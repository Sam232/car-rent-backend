package org.acme.rent.repo

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class RentRepo : PanacheRepositoryBase<Rent, Long> {}